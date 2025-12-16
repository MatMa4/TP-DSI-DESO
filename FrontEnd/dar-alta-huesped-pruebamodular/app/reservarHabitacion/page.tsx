'use client'; 
import React, { useState } from 'react';
import { InputField } from '../componentsCU4-5-15/InputField'; 
import ModalConfirmacion from '../componentsCU4-5-15/ModalConfirmacion'; 
import ModalError from '../componentsCU4-5-15/ModalError'; 
import FiltrosHabitacion from '../componentsCU4-5-15/Filtroshabitacion'; 
import DisponibilidadGrid from '../componentsCU4-5-15/DisponibilidadGrid'; 
import ReservaVerification from '../componentsCU4-5-15/ReservaVerification'; 
import EventualHuespedForm from '../componentsCU4-5-15/EventualHuespedForm'; 
import ModalFin from '../componentsCU4-5-15/ModalFinalizacion';
import { transformToGridData } from './transformToGridData';
import { useRouter } from 'next/navigation';
import '../styles/stylesCU4-5-15.css';
import { 
    RoomCellData, 
    SelectedReservation, 
    EventualHuesped,
    RoomStatusDTO,
} from '../types/indexCU4-5-15'; 

const RESERVA_STAGES = {
  GRILLA: 'GRILLA',
  HUESPED: 'HUESPED',
};

// --- COMPONENTE PRINCIPAL ---
export default function ReservarHabitacion() {
  const [stage, setStage] = useState(RESERVA_STAGES.GRILLA); 
  const [fechas, setFechas] = useState({ desde: '', hasta: '' });
  const [gridData, setGridData] = useState<RoomCellData[]>([]);
  const [selectedReservations, setSelectedReservations] = useState<SelectedReservation[]>([]);
  const [selectedRoomType, setSelectedRoomType] = useState<string>(''); 
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [showVerificationModal, setShowVerificationModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const router = useRouter();

  // --- LÓGICA DE VALIDACIÓN ---
  const getTodayDateString = () => {
    const d = new Date();
    const timezoneOffset = d.getTimezoneOffset() * 60000;
    const dateLocal = new Date(d.getTime() - timezoneOffset);
    const today = new Date();
    return dateLocal.toISOString().split('T')[0];
  };
  const validateFechas = (f: { desde: string, hasta: string }) => {
    const today = getTodayDateString();
    console.log(`Comparando: ${f.desde} < ${today}`);
    if (!f.desde || !f.hasta) return 'Debe seleccionar ambas fechas.';
    if (f.desde < today) return 'La fecha inicial debe ser posterior o igual a la fecha actual.';
    if (f.desde > f.hasta) return 'La fecha final no puede ser anterior a la fecha inicial.';
    return null;
  };

  const validateHuespedForm = (data: EventualHuesped): Record<string, string> => {
    const validationErrors: Record<string, string> = {};
    if (!data.apellido.trim()) {
        validationErrors.apellido = 'El apellido es obligatorio.';
        return validationErrors; 
    }
    if (!data.nombre.trim()) {
        validationErrors.nombre = 'El nombre es obligatorio.';
        return validationErrors; 
    }
    if (!data.telefono.trim()) {
        validationErrors.telefono = 'El teléfono es obligatorio.';
        return validationErrors; 
    }
    return validationErrors;
  };

  // --- MANEJADORES DE ESTADO Y FLUJO ---

  const handleFechasChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFechas(prev => ({ ...prev, [e.target.name]: e.target.value }));
    setErrors(prev => ({ ...prev, fechas: '' }));
    setGridData([]);
  };
  
  const handleSearch = async (tipo: string) => {
    
    const errorMsg = validateFechas(fechas);
    if (errorMsg) {
        setErrors({ fechas: errorMsg });
        return;
    }
    const BASE_URL = 'http://localhost:8080';
    const url = `${BASE_URL}/habitaciones?fechaInicio=${fechas.desde}&fechaFin=${fechas.hasta}`;;
    
    try {
      const response = await fetch(url);
      
      if (!response.ok) {
          // Manejar errores
          throw new Error(`Error ${response.status}: Fallo al conectar con la API de disponibilidad.`);
      }
      // 2. RECIBIR LA DATA
      const rawData: RoomStatusDTO[] = await response.json();
      
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
      // 3. ÉXITO
      setGridData(processedGridData);
      setSelectedRoomType(tipo);
    } catch (error) {
      setErrorMessage(`Hubo un error de conexión al buscar disponibilidad: ${error.message}`);
      setShowErrorModal(true);
      setGridData([]);
    }
};

const handleGridSubmit = (reservations: SelectedReservation[]) => {
    if (reservations.length === 0) return;   
    setSelectedReservations(reservations);
    setShowVerificationModal(true); 
};


const handleAcceptVerification = () => {
    setShowVerificationModal(false); 
    setStage(RESERVA_STAGES.HUESPED); 
};

const handleRejectVerification = () => {
    setShowVerificationModal(false); 
    setSelectedReservations([]); 
};

const handleHuespedSubmit = async (huespedData: EventualHuesped) => {
  const validationErrors = validateHuespedForm(huespedData);
  setErrors(validationErrors);

  if (Object.keys(validationErrors).length === 0) {
    
    if (selectedReservations.length === 0) {
        setErrorMessage("Error: No se encontró una selección de habitación válida.");
        setShowErrorModal(true);
        return;
    }

    const toISODate = (ymdString: string) => {
        return new Date(ymdString + 'T00:00:00').toISOString();
    };

    const payload = selectedReservations.map(reservation => ({
            "fechaInicio": toISODate(reservation.fechaInicio),
            "fechaFin": toISODate(reservation.fechaFin),
            "estado": "RESERVADA",
            "nombre": huespedData.nombre,
            "apellido": huespedData.apellido,
            "telefono": huespedData.telefono,
            "habitacionNumero": parseInt(reservation.roomId), 
    }));

    const BASE_URL = 'http://localhost:8080';
    const url = `${BASE_URL}/reservas`;

    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (response.status !== 201 && response.status !== 200) {

        let errorText = 'Fallo al registrar la reserva. Error desconocido.';
        try {
            const errorJson = await response.json();
            errorText = errorJson.message || JSON.stringify(errorJson);
        } catch (e) {
            errorText = await response.text(); 
        }
        throw new Error(`Error ${response.status}: ${errorText.substring(0, 200)}...`);
      }
      const cantidad = selectedReservations.length;
      setSuccessMessage(
        `Se han registrado con éxito ${cantidad} reserva(s) a nombre de ${huespedData.nombre} ${huespedData.apellido}. \nPresione cualquier tecla para continuar...`
      );

      setShowSuccessModal(true);
      setErrors({});

    } catch (error) {
      setErrorMessage(`Error al registrar la reserva: ${error.message}`);
      setShowErrorModal(true);
    }
  }
};
    
  const handleSuccessConfirm = () => {
    setShowSuccessModal(false);
    router.push('/');
  };

  const handleCancel = () => {
    setShowCancelModal(true);
  };

  const handleConfirmCancel = () => {
    setShowCancelModal(false);
    router.push('/');
  };

  // --- RENDERIZADO (UI) ---
  return (
    <main className="main-container">
      <div className="tittle_box">
        <h1 className="main_title">Reservar Habitación</h1>
      </div>

      <div className="main_box" style={{ padding: '20px' }}>
        
        {stage === RESERVA_STAGES.GRILLA && (
            <div className="full-grid-view">
                
                {/* 1. Filtros  */}
                <FiltrosHabitacion 
                    fechas={fechas} 
                    onChange={handleFechasChange}
                    onSearch={handleSearch} 
                    onCancel={handleCancel}
                    error={errors.fechas}
                />

                {/* 2. Grilla de Disponibilidad */}
                <div className='grid-container'style={{ 
                    border: gridData.length > 0 ? 'none' : '1px solid #ccc', 
                    backgroundColor: gridData.length > 0 ? 'transparent' : '#BEBCBC' 
                    }}>
                    {gridData.length > 0 ? (
                        <DisponibilidadGrid 
                            fechas={fechas} 
                            gridData={gridData}
                            onGridSubmit={handleGridSubmit}
                            onCancel={handleCancel}
                        />
                    ) : (
                        <p 
                          style={{ textAlign: 'center', padding: '100px', color: '#666' }}>
                          Seleccione fechas y tipo, luego presione "Mostrar Disponibilidad"
                        </p>
                    )}
                </div>
                
                {/* 3. Botones finales  */}
                <div className="modal-actions" 
                  style={{ display:'flex', marginTop: '20px', justifyContent: 'flex-start'}}>
                  <button className="btn-cancel" 
                  onClick={handleCancel}>Cancelar</button>
                </div>
            </div>
        )}

          <ReservaVerification 
            show={showVerificationModal} 
            reservations={selectedReservations} 
            onAccept={handleAcceptVerification} 
            onReject={handleRejectVerification} 
            onCancel={handleCancel}
          />
          
        {stage === RESERVA_STAGES.HUESPED && (
          <EventualHuespedForm 
            onSubmit={handleHuespedSubmit} 
            errors={errors} 
            onCancel={handleCancel}
          />
        )}

      </div>

      {/* Modales Globales */}
      <ModalConfirmacion
        show={showCancelModal}
        title="CANCELAR"
        message="¿Desea cancelar esta reserva? El caso de uso finalizará." 
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

    {showSuccessModal && successMessage && (
      <ModalFin
          show={showSuccessModal}
          message={successMessage}
          onConfirm={handleSuccessConfirm}
          onClose={() => setShowSuccessModal(false)}
      />
)}
    </main>
  );
}