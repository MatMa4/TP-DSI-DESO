import React from 'react';

// Definimos los tipos para las props del componentee
interface InputFieldProps {
  label: string;
  name: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void;
  error?: string;
  type?: string;
  isRequired?: boolean;
}

const InputField: React.FC<InputFieldProps> = ({
  label,
  name,
  value,
  onChange,
  error,
  type = 'text',
  isRequired = false,
}) => {
  return (
    <div className="box1_simplebox">
      <label htmlFor={name}>
        {label} {isRequired && <span style={{ color: 'red' }}>*</span>}
      </label>
      <input
        id={name}
        className="input_box"
        type={type}
        name={name}
        value={value}
        onChange={onChange}
      />
      {error && <p className="error">{error}</p>}
    </div>
  );
};

export default InputField;