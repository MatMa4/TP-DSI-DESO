'use client'
import React from 'react';

const ModalConfirmacion: React.FC<any> = ({
  show,
  title,
  message,
  icon,
  onClose,
  onConfirm,
  closeText,
  children,
  confirmText,
  confirmClass = 'btn-accept',
}) => {
  if (!show) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        <div className="modal-icon">{icon}</div>
        <h2>{title}</h2>
        <p>{message}</p>
        {children}
        <div className="modal-buttons">
          <button className="btn-cancel" 
          onClick={onClose}>
          {closeText}
          </button>
          <button className="btn-accept"
            className={confirmClass} 
            onClick={onConfirm}>
            {confirmText}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ModalConfirmacion;