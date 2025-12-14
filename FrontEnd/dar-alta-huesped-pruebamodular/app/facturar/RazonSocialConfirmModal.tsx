// src/components/RazonSocialConfirmModal.tsx
import React from 'react';

// Define las props
interface RazonSocialConfirmModalProps {
    show: boolean;
    razonSocial: string; // La Razón Social obtenida de la búsqueda
    onAccept: () => void; // Función para confirmar y avanzar al DetalleFacturaModal
    onCancel: () => void; // Función para rechazar y volver al CuitInputModal
}

const RazonSocialConfirmModal: React.FC<RazonSocialConfirmModalProps> = ({ 
    show,
    razonSocial, 
    onAccept, 
    onCancel 
}) => {

    if (!show) return null; // No renderizar si no está visible

    return (
        <div className="modal-overlay">
            <div className="modal-content" style={{ minHeight: '150px' }}> {/* Ajuste de altura si es necesario */}
                <h3 className="modal-title">Razón Social</h3>
                
                {/* Muestra la Razón Social para confirmación */}
                <p style={{ margin: '20px 0', fontSize: '1.1em', fontWeight: 'bold', textAlign: 'center' }}>
                    "{razonSocial}"
                </p>
                <div className="modal-actions">
                    <button className="btn-cancel" onClick={onCancel}>
                        CANCELAR
                    </button>
                    <button className="btn-accept" onClick={onAccept}>
                        ACEPTAR
                    </button>
                </div>
            </div>
        </div>
    );
};

export default RazonSocialConfirmModal;