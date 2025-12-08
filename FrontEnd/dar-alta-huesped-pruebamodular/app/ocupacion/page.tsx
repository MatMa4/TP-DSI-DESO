'use client'; 
import React, { useState, useEffect } from 'react';
import FiltrosHabitacion from '../componentsCU4-5-15/Filtroshabitacion';
import DisponibilidadGrid from '../componentsCU4-5-15/DisponibilidadGrid';
import ModalError from '../componentsCU4-5-15/ModalError'; 
import ReservaVerification from '../componentsCU4-5-15/ReservaVerification'; // Se reutiliza para mostrar la selección
import HuespedSearchAndSelect from '../componentsCU4-5-15/HuespedSearchAndSelect'; 
import { RoomCellData, SelectedReservation, generateGridData, HuespedSearchResult } from '../../types/indexCU4-5-15'; 

// Definición de las etapas del Caso de Uso Ocupar Habitación (CU15)
const OCUPAR_STAGES = {
    GRILLA_DISPONIBILIDAD: 'GRILLA_DISPONIBILIDAD', // 1. Selección de fechas/habitación
    VERIFICACION: 'VERIFICACION', // 2. Confirmación de selección de habitación
    BUSQUEDA_HUESPED: 'BUSQUEDA_HUESPED', // 3. Búsqueda y selección de huéspedes
    FINALIZADO: 'FINALIZADO', // 4. Ocupación y finalización
};

// --- COMPONENTE PRINCIPAL ---
export default function OcuparHabitacion() {
    const [stage, setStage] = useState(OCUPAR_STAGES.GRILLA_DISPONIBILIDAD); 
    const [fechas, setFechas] = useState({ desde: '', hasta: '' });
    const [gridData, setGridData] = useState<RoomCellData[]>([]);
    const [selectedRoomType, setSelectedRoomType] = useState<string>(''); 
    const [selectedReservations, setSelectedReservations] = useState<SelectedReservation[]>([]);
    const [occupyingGuests, setOccupyingGuests] = useState<HuespedSearchResult[]>([]); // Estado para guardar los huéspedes seleccionados

    // Control de modales y errores
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [showVerificationModal, setShowVerificationModal] = useState(false);
    
    // --- LÓGICA DE SIMULACIÓN DE DATOS (Reemplazar con fetch) ---
    const handleSearch = (tipo: string) => {
        if (!fechas.desde || !fechas.hasta) {
            setErrorMessage('Debe seleccionar ambas fechas.');
            setShowErrorModal(true);
            return;
        }

        // SIMULACIÓN (Reemplazar con la llamada GET al Back-End)
        const data = generateGridData(fechas.desde, fechas.hasta, tipo); 
        
        if (data.length === 0) {
            setErrorMessage("No existen habitaciones disponibles con los criterios solicitados.");
            setShowErrorModal(true);
            return;
        }
        
        setGridData(data);
        setSelectedRoomType(tipo);
        setErrorMessage('');
    };

    // --- MANEJADORES DE ESTADO Y FLUJO ---

    const handleFechasChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFechas(prev => ({ ...prev, [e.target.name]: e.target.value }));
        setGridData([]); 
    };
    
    // 1. Flujo: Después de seleccionar habitaciones en la grilla
    const handleGridSubmit = (reservations: SelectedReservation[]) => {
        if (reservations.length === 0) {
            setErrorMessage("Debe seleccionar una reserva o habitación para ocupar.");
            setShowErrorModal(true);
            return;
        }
        
        setSelectedReservations(reservations);
        setShowVerificationModal(true); // Mostrar modal de verificación (Resumen)
    };

    // 2. Flujo: Aceptar la verificación de la HABITACIÓN/FECHA
    const handleAcceptVerification = () => {
        setShowVerificationModal(false);
        // Pasa a la búsqueda y selección de Huéspedes
        setStage(OCUPAR_STAGES.BUSQUEDA_HUESPED); 
    };

    // 3. Flujo: El usuario selecciona los huéspedes (CRÍTICO: Nueva función)
    const handleHuespedSelectionSubmit = (selectedHuespedes: HuespedSearchResult[]) => {
        if (selectedHuespedes.length === 0) {
            setErrorMessage("Debe seleccionar al menos un huésped para asociar a la ocupación.");
            setShowErrorModal(true);
            return;
        }
        
        setOccupyingGuests(selectedHuespedes);
        
        // Aquí se realizaría la llamada final al Back-End (POST/PUT) para Ocupar.
        
        // Simulación de finalización exitosa:
        console.log("Datos de Ocupación Final:", { rooms: selectedReservations, guests: selectedHuespedes });
        setStage(OCUPAR_STAGES.FINALIZADO); 
    };
    
    // Flujo: Rechazar la selección o volver
    const handleRejectVerification = () => {
        setShowVerificationModal(false); 
        setSelectedReservations([]); 
    };

    // Flujo: Cancelar el Caso de Uso (Desde cualquier etapa)
    const handleCancel = () => {
        // En un CU real, esto debe usar el router.push('/') después de la confirmación
        alert("Caso de Uso CANCELADO. Se limpiarán los estados."); 
        setStage(OCUPAR_STAGES.GRILLA_DISPONIBILIDAD);
        setFechas({ desde: '', hasta: '' });
        setGridData([]);
        setSelectedReservations([]);
        setOccupyingGuests([]);
    };
    
    // --- RENDERIZADO (UI) ---
    return (
        <main className="main-container">
            <div className="tittle_box">
                <h1 className="main_title">Ocupar Habitación</h1>
            </div>

            <div className="main_box" style={{ padding: '20px' }}>
                
                {/* 1. Etapa GRILLA DE DISPONIBILIDAD */}
                {(stage === OCUPAR_STAGES.GRILLA_DISPONIBILIDAD || stage === OCUPAR_STAGES.VERIFICACION) && (
                    <div className="full-grid-view">
                        <FiltrosHabitacion 
                            fechas={fechas} 
                            onChange={handleFechasChange}
                            onSearch={handleSearch} 
                            onCancel={handleCancel}
                            error={''} 
                            searchButtonText="Mostrar Disponibilidad"
                        />
                        <div className='grid-container' style={{ 
                            border: gridData.length > 0 ? 'none' : '1px solid #ccc', 
                            backgroundColor: gridData.length > 0 ? 'transparent' : '#BEBCBC',
                            minHeight: '300px'
                        }}>
                            {gridData.length > 0 ? (
                                <DisponibilidadGrid 
                                    fechas={fechas} 
                                    gridData={gridData.filter(d => d.roomType === selectedRoomType)}
                                    onGridSubmit={handleGridSubmit} 
                                    onCancel={handleCancel}
                                    isReadOnly={false} 
                                />
                            ) : (
                                <p style={{ textAlign: 'center', padding: '100px', color: '#666' }}>
                                    Seleccione fechas y tipo, luego presione "Mostrar Disponibilidad"
                                </p>
                            )}
                        </div>
                        <div className="modal-actions" style={{ display:'flex', marginTop: '20px', justifyContent: 'flex-start'}}>
                            <button className="btn-cancel" onClick={handleCancel}>Cancelar</button>
                        </div>
                    </div>
                )}
                
                {/* 2. Etapa BÚSQUEDA Y SELECCIÓN DE HUÉSPEDES */}
                {stage === OCUPAR_STAGES.BUSQUEDA_HUESPED && (
                    <HuespedSearchAndSelect 
                        onSelectionSubmit={handleHuespedSelectionSubmit} 
                        onCancel={handleCancel}
                    />
                )}
                
                {/* 3. Etapa FINALIZADO */}
                {stage === OCUPAR_STAGES.FINALIZADO && (
                    <div style={{ textAlign: 'center', padding: '50px' }}>
                        <h2 style={{ color: 'green' }}>✅ Ocupación Registrada con Éxito</h2>
                        <p>Los huéspedes asociados son: {occupyingGuests.map(g => g.nombre).join(', ')}</p>
                    </div>
                )}
                
            </div>

            {/* Modales Globales */}
            <ReservaVerification 
                show={showVerificationModal} 
                reservations={selectedReservations} 
                onAccept={handleAcceptVerification} 
                onReject={handleRejectVerification} 
                onCancel={handleCancel}
            />
            <ModalError
                show={showErrorModal}
                message={errorMessage}
                onClose={() => setShowErrorModal(false)}
            />
        </main>
    );
}