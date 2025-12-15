'use client';
import React, { useState, useMemo } from 'react';
import ModalError from './ModalError';
import ModalConfirmacion from './ModalConfirmacion';
import { RoomCellData, SelectedReservation } from '../types/indexCU4-5-15'; 
import { HabitacionDTO } from '../facturar/interfaces';

interface DisponibilidadGridProps {
    fechas: { desde: string, hasta: string };
    gridData: RoomCellData[];
    onGridSubmit: (reservations: SelectedReservation[]) => void;
    onCancel: () => void;
}

interface ConflictoData {
    reservations: SelectedReservation[];
    conflictDetails: { date: string; user: string; dni: string; roomId: string }[];
}

const DisponibilidadGrid: React.FC<DisponibilidadGridProps> = ({ fechas, gridData, onGridSubmit }) => {
    const [selectionStart, setSelectionStart] = useState<string | null>(null);
    const [currentSelection, setCurrentSelection] = useState<Set<string>>(new Set());
    const [showConflictModal, setShowConflictModal] = useState(false);
    const [pendingConflict, setPendingConflict] = useState<ConflictoData | null>(null);
    const [conflictMessage, setConflictMessage] = useState('');
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    
    const dates = useMemo(() => {
        const uniqueDates = [...new Set(gridData.map(d => d.date))];
        return uniqueDates.sort();
    }, [gridData]);

    
    const actualRooms = (() => { 
        
        if (!gridData || gridData.length === 0) {
            return [];
        }

        const roomIds = gridData.map(d => d.roomId?.toString() ?? '');
        
        const validUniqueRooms = [...new Set(roomIds)].filter(id => id.length > 0);

        return validUniqueRooms.sort((a, b) => parseInt(a) - parseInt(b));
    })();

    const getCellState = (roomId: string, date: string): RoomCellData['estado'] => {
        return gridData.find(d => d.roomId === roomId && d.date === date)?.estado || 'Disponible';
    };

    const calculateRange = (startId: string, endId: string) => {
        const [startRoom, startDate] = startId.split('|');
        const [endRoom, endDate] = endId.split('|');
        
        // Determinar los índices de las fechas y habitaciones
        const allDates = dates.sort();
        const dateIndices = allDates.map((d, i) => [d, i]).filter(([d]) => 
            (d >= startDate && d <= endDate) || (d <= startDate && d >= endDate)
        ).map(([, i]) => i);
        
        const allRooms = ROOMS.sort();
        const startRoomIndex = allRooms.indexOf(startRoom);
        const endRoomIndex = allRooms.indexOf(endRoom);
        
        const minDateIndex = Math.min(...dateIndices);
        const maxDateIndex = Math.max(...dateIndices);
        const minRoomIndex = Math.min(startRoomIndex, endRoomIndex);
        const maxRoomIndex = Math.max(startRoomIndex, endRoomIndex);

        if (dateIndices.length === 0 || minRoomIndex === -1) return new Set<string>();

        const newSelection = new Set<string>();

        for (let i = minDateIndex; i <= maxDateIndex; i++) {
            for (let j = minRoomIndex; j <= maxRoomIndex; j++) {
                newSelection.add(`${allRooms[j]}|${allDates[i]}`);
            }
        }
        return newSelection;
    };

    const handleCellClick = (e: React.MouseEvent<HTMLTableDataCellElement>, roomId: string, date: string) => {
        const cellId = `${roomId}|${date}`;
        const estado = getCellState(roomId, date);
        
        // Validación inmediata (Flujo Alternativo 3.B/3.C)
        if (estado !== 'Disponible' && estado !== 'Reservada') {
            setErrorMessage(`La habitación ${roomId} está ${estado}. Solo se pueden seleccionar habitaciones Disponibles o Reservadas.`);
            setShowErrorModal(true);
            return;
        }
        
        if (e.detail === 2) {
            setCurrentSelection(new Set([cellId]));
            setSelectionStart(cellId);
            return;
        }

        if (e.shiftKey && selectionStart) {
            const range = calculateRange(selectionStart, cellId);

            for (const id of range) {
                const [rId, rDate] = id.split('|');
                const state = getCellState(rId, rDate);

                if (state !== 'Disponible' && state !== 'Reservada') {
                    setErrorMessage(`El rango seleccionado incluye habitaciones con estado "${state}".`);
                    setShowErrorModal(true);
                    return;
                }
            }
            setCurrentSelection(range);

        } else {

            setCurrentSelection(prev => {
                const next = new Set(prev);
                if (next.has(cellId)) {
                    next.delete(cellId);
                    setSelectionStart(null);
                } else {
                    next.add(cellId);
                    setSelectionStart(cellId);
                }
                return next;
            });
        }
    };
    
    const handleSubmit = () => {
        if (currentSelection.size === 0) {
            setErrorMessage('Debe seleccionar al menos una habitación.');
            setShowErrorModal(true);
            return;
        }
        
        // --- 1. PROCESAR SELECCIÓN ---
        const selected: SelectedReservation[] = [];
        const conflictDetails: ConflictoData['conflictDetails'] = [];

        // Lógica para agrupar celdas seleccionadas
        const roomsSelected = [...currentSelection].map(id => id.split('|')[0]);
        const uniqueRooms = [...new Set(roomsSelected)];
        
        uniqueRooms.forEach(roomId => {
            const datesSelected = [...currentSelection]
                .filter(id => id.startsWith(roomId))
                .map(id => id.split('|')[1])
                .sort();
            
            if (datesSelected.length > 0) {
                const type = gridData.find(d => d.roomId === roomId)?.roomType || 'N/A';
                selected.push({
                    roomId: roomId,
                    type: type,
                    fechaInicio: datesSelected[0],
                    fechaFin: datesSelected[datesSelected.length - 1]
                });
                
                 // --- 2. DETECCIÓN DE CONFLICTO ---
                 // Buscar celdas seleccionadas que ya estén 'Reservada'
                datesSelected.forEach(date => {
                    const cell = gridData.find(d => d.roomId === roomId && d.date === date);
                    
                    if (cell?.estado === 'Reservada') {

                        const reservaInfo = { 
                            user: cell.reservedBy || "Huésped no especificado", 
                            dni: cell.reservedDNI || "DNI no especificado",        
                        }; 
                        
                        conflictDetails.push({ 
                            date: date, 
                            user: reservaInfo.user, 
                            dni: reservaInfo.dni,         
                            roomId: roomId // ID de la habitación
                        });
                    }
                });
            }
        });

        const conflictData = { reservations: selected, conflictDetails: conflictDetails };
        
        // --- 3. MANEJO DEL FLUJO  ---
        if (conflictDetails.length > 0) {
            setPendingConflict(conflictData);
            setConflictMessage(
                `La/s habitación/es que ha seleccionado se encuentra/n reservada/s en las siguientes fechas. 
                \n¿Desea forzar la reserva de todas formas?`
            );
            setShowConflictModal(true);
        } else {
            onGridSubmit(selected); 
        }
    };
    
    // --- MANEJADORES DEL MODAL DE CONFLICTO ---
    const handleOccupyyAnyway = () => {
        if (pendingConflict) {
            onGridSubmit(pendingConflict.reservations); 
        }
        setShowConflictModal(false);
        setPendingConflict(null);
    };

    const handleBackToTable = () => {
        setShowConflictModal(false);
        setPendingConflict(null);
        setCurrentSelection(new Set());
    };

    // --- RENDERIZADO DEL MODAL ---
    const renderConflictDetails = () => (
    // Es bueno dar scroll por si hay muchos conflictos
    <div style={{ maxHeight: '200px', overflowY: 'auto', padding: '10px 0' }}> 
        {pendingConflict?.conflictDetails.map((c, index) => (
            <div key={index} style={{ borderBottom: '1px dotted #ccc', marginBottom: '10px', paddingBottom: '5px', textAlign: 'left' }}>
                <strong style={{ display: 'block' }}>Habitación {c.roomId}</strong>
                
                <p style={{ margin: '2px 0', fontSize: '14px' }}>
                    Fecha: {c.date} | Reservada.
                </p>
            </div>
        ))}
    </div>
);
    
    return (
        <div className="disponibilidad-grid-box">
            <div style={{ display: 'flex', gap: '20px', marginBottom: '15px' }}>
                <span style={{fontWeight: 'bold'}}>Desde: {fechas.desde} | Hasta: {fechas.hasta}</span>
                <div className="room-type-filter" style={{ marginLeft: 'auto' }}>
                </div>
            </div>
            {/* Tabla de Disponibilidad */}
            <div style={{ maxHeight: '400px', overflow: 'auto' }}>
                <table className="disponibilidad-table">
                    <thead>
                        <tr>
                            <th className='date-cell'>Fecha</th>
                            {actualRooms.map(roomId => (
                                <th key={roomId}>Habitación {roomId}</th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {dates.map(date => (
                            <tr key={date}>
                                <td className="date-cell">{date}</td>
                                {actualRooms.map(roomId => {
                                    const cellId = `${roomId}|${date}`;
                                    const estado = getCellState(roomId, date);
                                    
                                    let cellClass = `estado-${estado.replace(/\s/g, '')}`;
                                    if (currentSelection.has(cellId)) {
                                        cellClass += ' selected';
                                    }

                                    return (
                                        <td 
                                            key={cellId} 
                                            className={cellClass}
                                            onMouseDown={(e) => handleCellClick(e, roomId, date)}
                                        >
                                            {estado}
                                        </td>
                                    );
                                })}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            <div style={{ marginTop: '10px', display: 'flex' }}>
                <button 
                    className="btn-accept" 
                    onClick={handleSubmit} 
                    disabled={currentSelection.size === 0}
                    style={{ padding: '8px 15px', width: 'auto' }}
                >
                    Seleccionar
                </button>
            </div>
            
            {/* --- MODAL DE CONFLICTO DE RESERVA */}
            <ModalConfirmacion
                show={showConflictModal}
                title="⚠️ Conflicto de Reserva"
                message={conflictMessage}
                closeText="VOLVER"
                confirmText="OCUPAR IGUAL"
                confirmClass="btn-accept yellow" 
                onClose={handleBackToTable}
                onConfirm={handleOccupyyAnyway}
            >
                {renderConflictDetails()}
            </ModalConfirmacion>

            <ModalError 
                show={showErrorModal} 
                message={errorMessage} 
                onClose={() => setShowErrorModal(false)}
            />
        </div>
    );
};

export default DisponibilidadGrid;