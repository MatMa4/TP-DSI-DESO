import React from 'react';

// Tipos para las propss
interface DocumentoFieldCU2Props {
  tipoDocumento: string;
  numeroDocumento: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void;
  error?: string;
  highlight: boolean;
}

const DocumentoFieldCU2: React.FC<DocumentoFieldCU2Props> = ({
  tipoDocumento,
  numeroDocumento,
  onChange,
  error,
  highlight,
}) => {
  return (
    <div className="box1 boxDocumento">
      <label>Documento</label>
      <div className="documento_inputs">
        <select
          className="select_documento"
          name="tipoDocumento"
          value={tipoDocumento}
          onChange={onChange}
        >
          {/* Corregido: los values deben coincidir con la lógica de estado y validación (mayúsculas) */}
          <option value="">TIPO</option>
          <option value="DNI">DNI</option>
          <option value="LE">LE</option>
          <option value="LC">LC</option>
          <option value="PASAPORTE">PASAPORTE</option>
          <option value="OTRO">OTRO</option>
        </select>
        <input
          className="input_box_documento"
          type="text"
          name="numeroDocumento"
          value={numeroDocumento}
          onChange={onChange}

        />
      </div>
      {error && <p className="error">{error}</p>}
    </div>
  );
};

export default DocumentoFieldCU2;