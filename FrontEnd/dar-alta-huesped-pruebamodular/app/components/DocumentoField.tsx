import React from 'react';

// Tipos para las propss
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
          
          <option value="DNI">DNI</option>
          <option value="LE">LE</option>
          <option value="LC">LC</option>
          <option value="pasaporte">Pasaporte</option>
          <option value="Otro">Otro</option>
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