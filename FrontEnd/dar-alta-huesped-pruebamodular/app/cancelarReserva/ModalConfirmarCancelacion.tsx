
import React from 'react';
import { ReservaDTO } from './interfaces'; 

interface ModalConfirmarCancelacionProps {
    show: boolean;
    reservas: ReservaDTO[]; 
    onClose: () => void; 
    onConfirm: () => void; 
    isLoading: boolean;
}

const ModalConfirmarCancelacion: React.FC<ModalConfirmarCancelacionProps> = ({
    show,
    reservas,
    onClose,
    onConfirm,
    isLoading
}) => {

    if (!show || reservas.length === 0) return null;

    return (
        <div className="modal-overlay"> 
            <div className="modal" style={{ maxWidth: '450px' }}>
                <h3 className="modal-title" style={{ color: '#ef4444' }}>
                    🚨 Cancelar {reservas.length} Reserva(s)
                </h3>
                
                <p style={{ marginBottom: '15px', textAlign: 'center' }}>
                    ¿Está seguro de que desea cancelar las siguientes reservas seleccionadas?
                </p>

                <div style={{ 
                    maxHeight: '200px', 
                    overflowY: 'auto', 
                    border: '1px solid #ddd', 
                    borderRadius: '5px', 
                    backgroundColor: '#f9f9f9',
                    marginBottom: '20px',
                    padding: '10px'
                }}>
                    {reservas.map(reserva => (
                        <div key={reserva.idReserva} style={{ borderBottom: '1px solid #eee', padding: '8px 0', fontSize: '0.9em' }}>
                            <strong>#{reserva.idReserva}</strong> - Hab. {reserva.habitacionNumero} ({reserva.apellido})
                            <br/>
                            <span style={{color: '#666', fontSize: '0.85em'}}>
                                {reserva.fechaInicio} al {reserva.fechaFin}
                            </span>
                        </div>
                    ))}
                </div>

                <div className="modal-actions">
                    <button className="btn-cancel" onClick={onClose} disabled={isLoading}>
                        Volver
                    </button>
                    <button 
                        className="btn-cancelar-reserva" 
                        onClick={onConfirm}
                        disabled={isLoading}
                    >
                        {isLoading ? 'Procesando...' : 'Sí, Cancelar Seleccionadas'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ModalConfirmarCancelacion;