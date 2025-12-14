import React, { useState } from 'react';

// Define las props que este modal espera
interface CuitInputModalProps {
    show: boolean;
    onClose: () => void; // Para el botón "Atrás" o cerrar el modal
    onNext: (cuit: string) => void; // Para iniciar la búsqueda de Razón Social
}

const CuitInputModal: React.FC<CuitInputModalProps> = ({ show, onClose, onNext }) => {
    const [cuit, setCuit] = useState('');
    const [error, setError] = useState('');

    if (!show) return null; // No renderizar si no está visible

    const handleSiguiente = () => {
        // Validación básica (puedes mejorar esto)
        if (cuit.trim().length < 11) { 
            setError("Ingrese un CUIT válido (mínimo 11 dígitos).");
            return;
        }
        setError('');
        onNext(cuit.trim());
    };
    
    // Función para manejar el botón Atrás (vuelve a la selección de huésped)
    const handleAtras = () => {
        setCuit('');
        setError('');
        onClose();
    };

    return (
        <div className="modal-overlay"> 
            <div className="modal-content">
                <h3 className="modal-title">Ingrese el CUIT del responsable de pago</h3>
                
                <div className="input-group" style={{ marginBottom: '20px' }}>
                    <input 
                        id="cuit"
                        // Usamos text para permitir que el usuario ingrese guiones si lo desea,
                        // aunque la validación solo busca 11 números.
                        type="text" 
                        placeholder="XX-XXXXXXXX-X"
                        value={cuit}
                        onChange={(e) => setCuit(e.target.value.replace(/[^0-9]/g, ''))} // Limpia no-dígitos al escribir
                        className="modal-input" 
                        maxLength={11} // Limitamos la entrada a 11 dígitos
                    />
                    {error && <p className="error-message" style={{ color: 'red', fontSize: '0.9em' }}>{error}</p>}
                </div>

                <div className="modal-actions">
                    <button className="btn-cancel" onClick={handleAtras}>
                        Atrás
                    </button>
                    <button 
                        className="btn-accept" 
                        onClick={handleSiguiente} 
                        disabled={cuit.length < 11}
                    >
                        Siguiente
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CuitInputModal;