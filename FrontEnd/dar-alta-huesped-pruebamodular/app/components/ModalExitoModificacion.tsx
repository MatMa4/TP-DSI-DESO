import React from 'react';

interface ModalExitoModificacionProps {
  show: boolean;
  onConfirm: () => void;
}

const ModalExitoModificacion: React.FC<ModalExitoModificacionProps> = ({
  show,
  onConfirm,
}) => {
  if (!show) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        <div className="modal-icon">✅</div>
        <h2>¡ÉXITO!</h2>
        <p>La operación ha culminado con éxito</p>
        
        <div className="modal-buttons" style={{ justifyContent: 'center' }}>
          <button
            className="btn-accept"
            onClick={onConfirm}
          >
            VOLVER AL MENÚ
          </button>
        </div>
      </div>
    </div>
  );
};

export default ModalExitoModificacion;