'use client'; 
import React, { useState, useEffect } from 'react';
import FiltrosHabitacion from '../componentsCU4-5-15/Filtroshabitacion';
import DisponibilidadGrid from '../componentsCU4-5-15/DisponibilidadGrid';
import ModalError from '../componentsCU4-5-15/ModalError'; 
import ReservaVerification from '../componentsCU4-5-15/ReservaVerification'; 
import HuespedSearchAndSelect from '../componentsCU4-5-15/HuespedSearchAndSelect'; 
import ModalConfirmacion from '../componentsCU4-5-15/ModalConfirmacion';
import FlowDecisionModal from '../componentsCU4-5-15/FlowDecisionModal';
import { transformToGridData } from '../reservarHabitacion/transformToGridData';
import { RoomCellData, SelectedReservation, HuespedDTO,RoomStatusDTO } from '../types/indexCU4-5-15'; 
import { useRouter } from 'next/navigation';
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
    const [occupyingGuests, setOccupyingGuests] = useState<HuespedDTO[]>([]); 
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [showCancelModal, setShowCancelModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [showVerificationModal, setShowVerificationModal] = useState(false);
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [rawRoomData, setRawRoomData] = useState<RoomStatusDTO[]>([]);
    const router = useRouter();
    
const handleSearch = async (tipo: string) => {
    console.log('API de Disponibilidad Recargada')
    if (!fechas.desde || !fechas.hasta) {
        setErrorMessage('Debe seleccionar ambas fechas.');
        setShowErrorModal(true);
        return;
    }
    setErrorMessage(''); 
    setShowErrorModal(false);
    
    const BASE_URL = 'http://localhost:8080';
    const url = `${BASE_URL}/habitaciones?fechaInicio=${fechas.desde}&fechaFin=${fechas.hasta}&tipo=${tipo}`;

    try {
        const response = await fetch(url);
        
        if (!response.ok) {
            const errorBody = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error(`Error ${response.status}: ${errorBody.message || 'Fallo al conectar con la API de disponibilidad.'}`);
        }

        const rawData: RoomStatusDTO[] = await response.json(); 
        
        setRawRoomData(rawData);

        const processedGridData = transformToGridData(
            rawData,
            fechas.desde,
            fechas.hasta,
            tipo 
        );
        
        if (processedGridData.length === 0) {
            setErrorMessage("No existen habitaciones disponibles...");
            setShowErrorModal(true);
            setGridData([]);
            return;
        }

        setGridData(processedGridData);
        setSelectedRoomType(tipo); 
        
    } catch (error) {
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
    const handleSuccessConfirm = () => {
    setShowSuccessModal(false); 
    setStage(OCUPAR_STAGES.GRILLA_DISPONIBILIDAD);
    setFechas({ desde: '', hasta: '' });
    setGridData([]);
    setSelectedReservations([]);
    setOccupyingGuests([]);
    setSuccessMessage('');
    };

    const handleHuespedSelectionSubmit = async (selectedHuespedes: HuespedDTO[]) => {
        if (selectedHuespedes.length === 0) {
            setErrorMessage("Debe seleccionar al menos un huésped para asociar a la ocupación.");
            setShowErrorModal(true);
            return;
        }
        const roomSelection = selectedReservations[0];
        if (!roomSelection) {
            setErrorMessage("Error: La habitación y fechas seleccionadas se perdieron.");
            setShowErrorModal(true);
            return;
        }

        const habitacionDTOCompleta = rawRoomData.find(
        (roomDto) => roomDto.habitacion.numero.toString() === roomSelection.roomId
        );

        if (!habitacionDTOCompleta) {
            setErrorMessage("Error interno: No se encontraron los detalles completos de la habitación para el registro.");
            setShowErrorModal(true);
            return;
        }
        const h = habitacionDTOCompleta.habitacion;

        const toISODate = (ymdString: string) => {
        return new Date(ymdString + 'T00:00:00').toISOString(); 
        };
        const tipoHabitacionLimpio = selectedRoomType.replace(/\s/g, '');
        const payload = {
        "habitacion": { 
            "numero": parseInt(roomSelection.roomId),
            "costoPorNoche": h.costoPorNoche,
            "capacidad": h.capacidad,
            "estado": h.estado,
            "descripcion": h.descripcion,
            "camaDoble": h.camaDoble,
            "tipoHabitacion": tipoHabitacionLimpio, 
        },
        "tipoHabitacion": tipoHabitacionLimpio,
        "fechaInicio": toISODate(roomSelection.fechaInicio),
        "fechaFin": toISODate(roomSelection.fechaFin),
        "checkIn": "14:00:00", 
        "checkOut": "10:00:00", 
        
        "huespedes": selectedHuespedes.map(huesped => ({
            "numeroDocumento": huesped.numeroDocumento,
            "tipoDocumento": huesped.tipoDocumento,
            "apellido": huesped.apellido,
            "nombre": huesped.nombre,
            "fechaNacimiento": huesped.fechaNacimiento,
            "telefono": huesped.telefono,
            "email": huesped.email,
            "ocupacion": huesped.ocupacion,
            "nacionalidad": huesped.nacionalidad,
            "cuit": huesped.cuit,
            "posicionIVA": huesped.posicionIVA,
            "alojado": true,
            "direccionHuesped": huesped.direccionHuesped, 
        }))
    };
        
        const BASE_URL = 'http://localhost:8080';
        const url = `${BASE_URL}/ocupacion`; 

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.status !== 201 && response.status !== 200) {
                let errorText = 'Fallo al registrar la ocupación.';
                try {

                errorText = await response.text(); 
            } catch (e) { /* ignore */ }
            throw new Error(`Error ${response.status}: ${errorText.substring(0, 200)}...`); 
        }
        // Éxito
        setOccupyingGuests(selectedHuespedes);
        setSuccessMessage(`Ocupación registrada con éxito para la Habitación ${roomSelection.roomId}.`);
        setShowSuccessModal(true);
        setStage(OCUPAR_STAGES.FINALIZADO);
        
        } catch (error) {
            setErrorMessage(`Error al registrar la ocupación: ${error.message}`);
            setShowErrorModal(true);
        }
    };

    const resetStageForNewSearch = () => {
    setFechas({ desde: '', hasta: '' });
    setGridData([]);
    setSelectedReservations([]);
    setOccupyingGuests([]);
    setSuccessMessage('');
    setShowSuccessModal(false);
    setStage(OCUPAR_STAGES.GRILLA_DISPONIBILIDAD);
    };


    const handleContinueLoading = () => {
        setShowSuccessModal(false); 
        setStage(OCUPAR_STAGES.BUSQUEDA_HUESPED); 
        setOccupyingGuests([]); 
    };

    const handleLoadAnotherRoom = () => {
        resetStageForNewSearch();
        const tipo = selectedRoomType; 
        setTimeout(() => {
        handleSearch(tipo);
    }, 100);
    };

    const handleExit = () => {
        resetStageForNewSearch();
        router.push('/');
    };

    const handleRejectVerification = () => {
        setShowVerificationModal(false); 
        setSelectedReservations([]); 
    };

    const handleCancel = () => {
        setShowCancelModal(true);
        setStage(OCUPAR_STAGES.GRILLA_DISPONIBILIDAD);
        setFechas({ desde: '', hasta: '' });
        setGridData([]);
        setSelectedReservations([]);
        setOccupyingGuests([]);
    };

    const handleConfirmCancel = () => {
    setShowCancelModal(false);
    router.push('/');
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
                            
                        />
                        <div className='grid-container' style={{ 
                            border: gridData.length > 0 ? 'none' : '1px solid #ccc', 
                            backgroundColor: gridData.length > 0 ? 'transparent' : '#BEBCBC',
                            minHeight: '300px'
                        }}>
                            {gridData.length > 0 ? (
                                <DisponibilidadGrid 
                                    fechas={fechas} 
                                    gridData={gridData}
                                    onGridSubmit={handleGridSubmit} 
                                    onCancel={handleCancel}
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
                    <FlowDecisionModal
                        show={showSuccessModal}
                        message={successMessage}
                        onContinue={handleContinueLoading}
                        onAnotherRoom={handleLoadAnotherRoom}
                        onExit={handleExit}
                    />
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
            <ModalConfirmacion
                show={showCancelModal}
                title="CANCELAR"
                message="¿Desea cancelar esta Ocupación? El caso de uso finalizará." 
                icon="⚠️"
                closeText="NO"
                confirmText="SI"
                onClose={() => setShowCancelModal(false)}
                onConfirm={handleConfirmCancel}
            />
            <ModalError
                show={showErrorModal}
                message={errorMessage}
                onClose={() => setShowErrorModal(false)}
            />
        </main>
    );
}