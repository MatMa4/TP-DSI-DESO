'use client';
import React from 'react';
import '../styles/stylesCU4-5-15.css'; // Asegúrate de que esta ruta sea correcta

interface FlowDecisionModalProps {
    show: boolean;
    message: string;
    // Callbacks para las tres acciones del flujo de trabajo
    onContinue: () => void; // Seguir cargando huéspedes (CU15: BUSQUEDA_HUESPED)
    onAnotherRoom: () => void; // Cargar otra habitación (Volver a GRILLA_DISPONIBILIDAD)
    onExit: () => void; // Salir (Volver al menú principal)
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
                    
                    {/* Botón 1: Seguir Cargando Huéspedes */}
                    <button 
                        className="btn-accept" 
                        onClick={onContinue}
                    >
                        Seguir Cargando
                    </button>

                    {/* Botón 2: Cargar Otra Habitación */}
                    <button 
                        className="btn-accept" 
                        onClick={onAnotherRoom}
                    >
                        Cargar Otra Habitación
                    </button>
                    
                    {/* Botón 3: Salir */}
                    <button 
                        className="btn-cancel" // Puedes usar una clase de botón diferente para "Salir"
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