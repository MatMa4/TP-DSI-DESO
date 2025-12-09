import React from 'react';

// Tipos para las propss
interface ModalConfirmacionProps {
  show: boolean;
  title: string;
  message: string | React.ReactNode;
  icon: string;
  onClose: () => void;
  onConfirm: () => void;
  closeText: string;
  confirmText: string;
  confirmClass?: string;
}

const ModalConfirmacion: React.FC<ModalConfirmacionProps> = ({
  show,
  title,
  message,
  icon,
  onClose,
  onConfirm,
  closeText,
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
        <div className="modal-buttons">
          <button className="btn-cancel" onClick={onClose}>
            {closeText}
          </button>
          <button className={confirmClass} onClick={onConfirm}>
            {confirmText}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ModalConfirmacion;