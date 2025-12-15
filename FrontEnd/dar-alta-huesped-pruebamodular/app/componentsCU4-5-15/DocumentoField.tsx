'use client'
import React from 'react';

interface DocumentoFieldProps {
  tipoDocumento: string;
  numeroDocumento: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void;
  error?: string;
  highlight: boolean;
}

const DocumentoField: React.FC<DocumentoFieldProps> = ({
  tipoDocumento,
  numeroDocumento,
  onChange,
  error,
  highlight,
}) => {
  return (
    <div className="box1 boxDocumento">
      <label>Documento <span style={{ color: 'red' }}>*</span></label>
      <div className="documento_inputs">
        <select
          className="input_box"
          name="tipoDocumento"
          value={tipoDocumento}
          onChange={onChange}
          style={highlight ? { borderColor: 'red', borderWidth: '3px' } : {}}
        >
          {/* Corregido: los values deben coincidir con la lógica de estado y validación (mayúsculas) */}
          <option value="">TIPO</option>
          <option value="DNI">DNI</option>
          <option value="LE">LE</option>
          <option value="LC">LC</option>
          <option value="PASAPORTE">Pasaporte</option>
          <option value="OTRO">Otro</option>
        </select>
        <input
          className="input_box_documento"
          type="text"
          name="numeroDocumento"
          value={numeroDocumento}
          onChange={onChange}
          style={highlight ? { borderColor: 'red', borderWidth: '3px' } : {}}
        />
      </div>
      {error && <p className="error">{error}</p>}
    </div>
  );
};

export default DocumentoField;