'use client'
import React, { useEffect, useCallback } from 'react';

interface ModalExitoProps {
show: boolean;
message: string;
onClose: () => void;
onConfirm: () => void;
}

const ModalExito: React.FC<ModalExitoProps> = ({
show,
message,
onClose,
onConfirm,
}) => {

const handleKeyPress = useCallback((e: KeyboardEvent) => {
if (!show) return;

e.preventDefault();
e.stopPropagation();
onConfirm(); 
}, [onConfirm, show]); 

useEffect(() => {
let timeoutId: NodeJS.Timeout | null = null;

if (show) {
    timeoutId = setTimeout(() => {
        window.addEventListener('keydown', handleKeyPress);
    }, 100);
}

return () => {
    if (timeoutId) {
    clearTimeout(timeoutId);
    }
    window.removeEventListener('keydown', handleKeyPress);
};
}, [show, handleKeyPress]);

if (!show) return null;

return (
<div className="modal-overlay">
    <div className="modal">
    <div className="modal-icon">✅</div>
    <h2>Completado</h2>
    <p>{message}</p>
    </div>
</div>
);
};

export default ModalExito;