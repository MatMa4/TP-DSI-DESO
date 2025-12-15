'use client'; 
import React from 'react';
import '../styles/stylesCU4-5-15.css'

interface ModalErrorProps {
    show: boolean;
    message: string;
    onClose: () => void;
}

    const ModalError: React.FC<ModalErrorProps> = ({ show, message, onClose }) => {
        if (!show) return null;
        
        return (
        <div className="modal-overlay"> 
            <div className="modal" style={{ width: '450px' }}> 
                <h2 style={{ color: '#E74C3C', borderBottom: '1px solid #eee', paddingBottom: '10px' }}>
                    ⚠️ ERROR
                </h2>
                <p style={{ whiteSpace: 'pre-wrap', marginBottom: '20px' }}>{message}</p>
                <div className="modal-buttons" style={{ justifyContent: 'flex-end' }}>
                    <button className="btn-cancel" onClick={onClose}>Cerrar</button>
                </div>
            </div>
        </div>
    );
};

export default ModalError;