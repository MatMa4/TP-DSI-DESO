 import React from 'react';
import InputField from './InputField';
import { Direccion } from '../page';

// Tipos para las props
interface DireccionHuespedProps {
  direccion: Direccion;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void;
  errors: Record<string, string>; // Pasamos todos los errores
}

const DireccionHuesped: React.FC<DireccionHuespedProps> = ({
  direccion,
  onChange,
  errors,
}) => {
  return (
    <>
      <div className="container">
        <div className="box1">
          <InputField
            label="Calle"
            name="direccionHuesped.calle"
            value={direccion.calle}
            onChange={onChange}
            error={errors['direccionHuesped.calle']}
            isRequired={true}
          />
          <InputField
            label="Codigo Postal"
            name="direccionHuesped.codigo"
            value={direccion.codigo}
            onChange={onChange}
            error={errors['direccionHuesped.codigo']}
          />
        </div>
        <div className="box1">
          <InputField
            label="Número"
            name="direccionHuesped.numero"
            value={direccion.numero}
            onChange={onChange}
            error={errors['direccionHuesped.numero']}
            isRequired={true}
          />
          <InputField
            label="Localidad"
            name="direccionHuesped.localidad"
            value={direccion.localidad}
            onChange={onChange}
            error={errors['direccionHuesped.localidad']}
            isRequired={true}
          />
        </div>
        <div className="box1">
          <InputField
            label="Piso"
            name="direccionHuesped.piso"
            value={direccion.piso}
            onChange={onChange}
            error={errors['direccionHuesped.piso']}
            isRequired={true}
          />
          <InputField
            label="Provincia"
            name="direccionHuesped.provincia"
            value={direccion.provincia}
            onChange={onChange}
            error={errors['direccionHuesped.provincia']}
            isRequired={true}
          />
        </div>
        <div className="box1">
          <InputField
            label="Departamento"
            name="direccionHuesped.departamento"
            value={direccion.departamento}
            onChange={onChange}
            error={errors['direccionHuesped.departamento']}
            isRequired={true}
          />
          <InputField
            label="País"
            name="direccionHuesped.pais"
            value={direccion.pais}
            onChange={onChange}
            error={errors['direccionHuesped.pais']}
            isRequired={true}
          />
        </div>
      </div>
    </>
  );
};

export default DireccionHuesped;