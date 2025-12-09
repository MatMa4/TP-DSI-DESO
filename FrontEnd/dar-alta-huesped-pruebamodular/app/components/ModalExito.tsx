import React from 'react';

// Tipos para las props
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
  if (!show) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        <div className="modal-icon">✅</div>
        <h2>Éxito al cargar</h2>
        <p>{message}</p>
        <div className="modal-buttons" style={{ justifyContent: 'center', gap: '20px' }}>
          <button
            className="btn-cancel"
            onClick={onClose}
          >
            NO
          </button>
          <button
            className="btn-accept"
            onClick={onConfirm}
          >
            SI
          </button>
        </div>
      </div>
    </div>
  );
};

export default ModalExito;