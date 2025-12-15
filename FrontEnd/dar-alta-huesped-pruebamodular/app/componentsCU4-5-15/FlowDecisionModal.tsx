'use client';
import React from 'react';
import '../styles/stylesCU4-5-15.css'; 

interface FlowDecisionModalProps {
    show: boolean;
    message: string;
    onContinue: () => void; 
    onAnotherRoom: () => void; 
    onExit: () => void; 
}

const FlowDecisionModal: React.FC<FlowDecisionModalProps> = ({ 
    show, 
    message, 
    onContinue, 
    onAnotherRoom, 
    onExit 
}) => {
    if (!show) {
        return null;
    }

    return (
        <div className="modal-overlay">
            <div className="modal" style={{ maxWidth: '500px' }}>
                <div className="modal-header">
                    <h2 style={{ color: '#008000' }}>✅ Operación Exitosa</h2>
                </div>
                <div className="modal-body">
                    <p style={{ textAlign: 'center', marginBottom: '20px' }}>{message}</p>
                </div>
                
                <div className="modal-footer" style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>

                    <button 
                        className="btn-accept" 
                        onClick={onContinue}
                    >
                        Seguir Cargando
                    </button>

                    <button 
                        className="btn-accept" 
                        onClick={onAnotherRoom}
                    >
                        Cargar Otra Habitación
                    </button>

                    <button 
                        className="btn-cancel" 
                        onClick={onExit}
                    >
                        Salir
                    </button>
                </div>
            </div>
        </div>
    );
};

export default FlowDecisionModal;