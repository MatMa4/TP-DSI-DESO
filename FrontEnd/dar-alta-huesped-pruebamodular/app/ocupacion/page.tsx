'use client'; 
import React, { useState, useEffect } from 'react';
import FiltrosHabitacion from '../componentsCU4-5-15/Filtroshabitacion';
import DisponibilidadGrid from '../componentsCU4-5-15/DisponibilidadGrid';
import ModalError from '../componentsCU4-5-15/ModalError'; 
import ReservaVerification from '../componentsCU4-5-15/ReservaVerification'; 
import HuespedSearchAndSelect from '../componentsCU4-5-15/HuespedSearchAndSelect'; 
import { RoomCellData, SelectedReservation, HuespedDTOCompleto } from '../types/indexCU4-5-15'; 
import '../styles/stylesCU4-5-15.css';

const OCUPAR_STAGES = {
    GRILLA_DISPONIBILIDAD: 'GRILLA_DISPONIBILIDAD', 
    VERIFICACION: 'VERIFICACION', 
    BUSQUEDA_HUESPED: 'BUSQUEDA_HUESPED', 
    FINALIZADO: 'FINALIZADO',
};

// --- COMPONENTE PRINCIPAL ---
export default function OcuparHabitacion() {
    const [stage, setStage] = useState(OCUPAR_STAGES.GRILLA_DISPONIBILIDAD); 
    const [fechas, setFechas] = useState({ desde: '', hasta: '' });
    const [gridData, setGridData] = useState<RoomCellData[]>([]);
    const [selectedRoomType, setSelectedRoomType] = useState<string>(''); 
    const [selectedReservations, setSelectedReservations] = useState<SelectedReservation[]>([]);
    const [occupyingGuests, setOccupyingGuests] = useState<HuespedDTOCompleto[]>([]); 
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [showVerificationModal, setShowVerificationModal] = useState(false);
    
    // --- LÓGICA DE SIMULACIÓN DE DATOS 
const handleSearch = async (tipo: string) => {
    if (!fechas.desde || !fechas.hasta) {
        setErrorMessage('Debe seleccionar ambas fechas.');
        setShowErrorModal(true);
        return;
    }
    
    // CRÍTICO: Limpieza de estado de error antes de buscar
    setErrorMessage(''); 
    setShowErrorModal(false);
    
    const BASE_URL = 'http://localhost:8080';
    const url = `${BASE_URL}/api/habitaciones/disponibilidad?fechaInicio=${fechas.desde}&fechaFin=${fechas.hasta}&tipo=${tipo}`;

    try {
        const response = await fetch(url);
        
        if (!response.ok) {
            const errorBody = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error(`Error ${response.status}: ${errorBody.message || 'Fallo al conectar con la API de disponibilidad.'}`);
        }
        
        // 2. RECIBIR LA DATA
        const data: RoomCellData[] = await response.json(); 
        
        if (!Array.isArray(data) || data.length === 0) {
            setErrorMessage("No existen habitaciones disponibles con las comodidades deseadas para el rango de fechas solicitado.");
            setShowErrorModal(true);
            setGridData([]);
            return;
        }
        
        // 3. ÉXITO
        setGridData(data);
        setSelectedRoomType(tipo); // Establecer el tipo de habitación seleccionado
        
    } catch (error) {
        // Captura errores de red o los lanzados en el bloque try
        setErrorMessage(`Hubo un error de conexión al buscar disponibilidad: ${error.message}`);
        setShowErrorModal(true);
        setGridData([]);
    }
};
    // --- MANEJADORES DE ESTADO Y FLUJO ---

    const handleFechasChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFechas(prev => ({ ...prev, [e.target.name]: e.target.value }));
        setGridData([]); 
    };
    
    const handleGridSubmit = (reservations: SelectedReservation[]) => {
        if (reservations.length === 0) {
            setErrorMessage("Debe seleccionar una reserva o habitación para ocupar.");
            setShowErrorModal(true);
            return;
        }
        
        setSelectedReservations(reservations);
        setShowVerificationModal(true);
    };

    const handleAcceptVerification = () => {
        setShowVerificationModal(false);
        setStage(OCUPAR_STAGES.BUSQUEDA_HUESPED); 
    };

    const handleHuespedSelectionSubmit = async (selectedHuespedes: HuespedDTOCompleto[]) => {
        if (selectedHuespedes.length === 0) {
            setErrorMessage("Debe seleccionar al menos un huésped para asociar a la ocupación.");
            setShowErrorModal(true);
            return;
        }
        
        // Asumimos que la ocupación se hace sobre la primera selección de la grilla (ya que el DTO es singular)
        const roomSelection = selectedReservations[0];
        if (!roomSelection) {
            setErrorMessage("Error: La habitación y fechas seleccionadas se perdieron.");
            setShowErrorModal(true);
            return;
        }

        // 1. CONSTRUIR EL PAYLOAD (OcupacionDTO)
        const payload = {
        "habitacion": { "numero": parseInt(roomSelection.roomId) },
        "fechaInicio": roomSelection.fechaInicio,
        "fechaFin": roomSelection.fechaFin,
        "checkIn": "14:00:00", 
        "checkOut": "10:00:00", 
        
        // C. Array de Huéspedes: Usamos los objetos completos tal como fueron recibidos
        "huespedes": selectedHuespedes.map(h => ({
            // Mapeamos los campos que el Back-End espera, usando el objeto 'h' completo
            "numeroDocumento": h.numeroDocumento,
            "tipoDocumento": h.tipoDocumento,
            "apellido": h.apellido,
            "nombre": h.nombre,
            "fechaNacimiento": h.fechaNacimiento,
            "telefono": h.telefono,
            "email": h.email,
            "ocupacion": h.ocupacion,
            "nacionalidad": h.nacionalidad,
            "cuit": h.cuit,
            "posicionIVA": h.posicionIVA,
            "alojado": true,
            "direccionHuesped": h.direccionHuesped, // Objeto anidado completo
        }))
    };
        
        const BASE_URL = 'http://localhost:8080';
        const url = `${BASE_URL}/api/ocupaciones`; // Endpoint de Ocupación

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.status !== 201 && response.status !== 200) {
                const errorText = await response.text();
                throw new Error(errorText || 'Fallo al registrar la ocupación.');
            }

            // Éxito
            setStage(OCUPAR_STAGES.FINALIZADO);
            // Podrías usar setSuccessMessage aquí para mostrar el éxito en el modal de CU4/15
            
        } catch (error) {
            setErrorMessage(`Error al registrar la ocupación: ${error.message}`);
            setShowErrorModal(true);
        }
    };

    const handleRejectVerification = () => {
        setShowVerificationModal(false); 
        setSelectedReservations([]); 
    };

    const handleCancel = () => {
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
            {(stage === OCUPAR_STAGES.GRILLA_DISPONIBILIDAD || stage === OCUPAR_STAGES.VERIFICACION) && (
            <div className="tittle_box">
                <h1 className="main_title">Ocupar Habitación</h1>
            </div>
            )}
            <div className="main_box" style={{ padding: '20px' }}>
                
                {/*GRILLA DE DISPONIBILIDAD */}
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
                
                {/* BÚSQUEDA Y SELECCIÓN DE HUÉSPEDES */}
                {stage === OCUPAR_STAGES.BUSQUEDA_HUESPED && (
                    <HuespedSearchAndSelect 
                        onSelectionSubmit={handleHuespedSelectionSubmit} 
                        onCancel={handleCancel}
                    />
                )}
                
                {/* Etapa FINALIZADO */}
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