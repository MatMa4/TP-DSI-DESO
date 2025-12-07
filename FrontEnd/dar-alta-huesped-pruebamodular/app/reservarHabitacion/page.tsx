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
import { 
    RoomCellData, 
    SelectedReservation, 
    EventualHuesped, 
    generateGridData 
} from '../types/indexCU4-5-15'; 



const RESERVA_STAGES = {
  GRILLA: 'GRILLA',
  HUESPED: 'HUESPED',
  FINALIZADO: 'FINALIZADO',
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

  // --- LÓGICA DE VALIDACIÓN ---
  const validateFechas = (f: { desde: string, hasta: string }) => {
    const today = new Date().toISOString().split('T')[0];
    if (!f.desde || !f.hasta) return 'Debe seleccionar ambas fechas.';
    if (f.desde < today) return 'La fecha inicial debe ser posterior o igual a la fecha actual.';
    if (f.desde > f.hasta) return 'La fecha inicial no puede ser posterior a la fecha final.';
    return '';
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
    setGridData([]); // Limpiar grilla al cambiar fechas
  };
  
  // Lógica de búsqueda 
  const handleSearch = (tipo: string) => {
    const errorMsg = validateFechas(fechas);
    if (errorMsg) {
      setErrors({ fechas: errorMsg });
      return;
    }

    // 1. Guarda el tipo seleccionado
    setSelectedRoomType(tipo);
    
    // 2. Simulación de carga de datos 
    const data = generateGridData(fechas.desde, fechas.hasta,tipo); // generateGridData debe usar el tipo como filtro
    
    if (data.length === 0) {
        setErrorMessage("No existen habitaciones disponibles con las comodidades deseadas para el rango de fechas solicitado.");
        setShowErrorModal(true);
        return;
    }
    
    setGridData(data);
    setErrors({});
  };

  // 2. Lógica para la selección de la Grilla y pasar a VERIFICACION
const handleGridSubmit = (reservations: SelectedReservation[]) => {
    if (reservations.length === 0) return;
    
    setSelectedReservations(reservations);
    // En lugar de cambiar de stage, mostramos el modal
    setShowVerificationModal(true); 
    // Mantenemos el stage en GRILLA o creamos un stage VERIFICANDO para bloquear la UI detrás.
};

// 3. Lógica para ACEPTAR la Verificación y pasar al formulario de Huésped (Paso 7)
const handleAcceptVerification = () => {
    setShowVerificationModal(false); // Cierra el modal
    setStage(RESERVA_STAGES.HUESPED); // Pasa al formulario de Huésped
};

// 3. Lógica para RECHAZAR la Verificación (Flujo Alternativo 7.A)
const handleRejectVerification = () => {
    setShowVerificationModal(false); // Cierra el modal
    setSelectedReservations([]); // Deshace la selección
};

  const handleHuespedSubmit = (data: EventualHuesped) => {
    const validationErrors = validateHuespedForm(data);
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length === 0) {
      console.log("✅ Reserva Registrada:", { selectedReservations, huesped: data });
        setSuccessMessage(
        `La reserva para ${data.nombre} ${data.apellido} ha sido realizada con éxito. \nPresione cualquier tecla para continuar...`
      );
      setShowSuccessModal(true);
      setErrors({});
    } else {
      setErrors(validationErrors);
      const firstErrorField = Object.keys(validationErrors)[0];
      setErrorMessage('Faltan completar campos obligatorios.');
      setShowErrorModal(true);
    }
  };
    
  const handleSuccessConfirm = () => {
    setShowSuccessModal(false);
    setStage(RESERVA_STAGES.FINALIZADO);
  };

  // Lógica de CANCELAR (Observación)
  const handleCancel = () => {
    setShowCancelModal(true);
  };

  const handleConfirmCancel = () => {
    setShowCancelModal(false);
    setShowVerificationModal(false);
    setStage(RESERVA_STAGES.FINALIZADO);
  };

  const currentStageName = Object.keys(RESERVA_STAGES).find(key => RESERVA_STAGES[key as keyof typeof RESERVA_STAGES] === stage);

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
                            gridData={gridData.filter(d => d.roomType === selectedRoomType)}
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
        
        {stage === RESERVA_STAGES.FINALIZADO && (
        <div>
            <h2 style={{ 
              marginTop:'150px',
                color: '#022E66', 
                fontSize: '36px', 
                marginBottom: '20px' 
            }}>
                ✅ Caso de Uso Finalizado.
            </h2>
        </div>
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