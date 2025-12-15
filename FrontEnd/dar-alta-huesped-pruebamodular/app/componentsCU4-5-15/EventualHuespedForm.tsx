'use client';
import React, { useState } from 'react';
import { InputField } from './InputField';
import { EventualHuesped } from '../types/indexCU4-5-15'; 

interface EventualHuespedFormProps {
    onSubmit: (data: EventualHuesped) => void;
    onCancel: () => void;
    errors: Record<string, string>;
}

    const EventualHuespedForm: React.FC<EventualHuespedFormProps> = ({ onSubmit, onCancel, errors }) => {
        const [data, setData] = useState<EventualHuesped>({ nombre: '', apellido: '', telefono: '' });

        const handleChange = (e: React.ChangeEvent<any>) => {
        const { name, value } = e.target;

        let processedValue = value;

        if (name === 'telefono') {
            processedValue = value.replace(/\D/g, ''); 
        } else {
            processedValue = value.toUpperCase();
        }
        setData(prev => ({ ...prev, [name]: processedValue }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(data);
    };

    return (
        <form className="huesped-form-container" onSubmit={handleSubmit}>
            <h2 style={{ 
                textAlign: 'center', 
                color: '#022E66', 
                fontSize: '30px', 
                marginBottom: '30px', 
                borderBottom: '2px solid #D9D9D9',
                paddingBottom: '10px',
                fontFamily:'cursive',
            }}>
                Datos del Huésped 
            </h2>
            
            {/* RECUADRO GRIS */}
            <div className="form-data-card" style={{ 
                backgroundColor: '#D9D9D9', 
                padding: '30px', 
                borderRadius: '8px',
                maxWidth: '600px',
                margin: '0 auto',
                boxShadow: '0 5px 8px rgba(0, 0, 0, 0.66)'
            }}>
                <h3 style={{ 
                    color: '#004d99', 
                    fontSize: '18px', 
                    marginBottom: '20px', 
                    textAlign: 'left'
                }}>
                    Reserva a nombre de:
                </h3>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                    <InputField
                        label="Apellido"
                        name="apellido"
                        value={data.apellido}
                        onChange={handleChange}
                        error={errors.apellido}
                        isRequired={true}
                    />
                    <InputField
                        label="Nombre"
                        name="nombre"
                        value={data.nombre}
                        onChange={handleChange}
                        error={errors.nombre}
                        isRequired={true}
                    />
                    <InputField
                        label="Teléfono"
                        name="telefono"
                        value={data.telefono}
                        onChange={handleChange}
                        error={errors.telefono}
                        isRequired={true}
                        type="tel"
                    />
                </div>
            </div>

            {/* Botones al fondo del formulario */}
            <div className="modal-actions" style={{ justifyContent: 'flex-end', marginTop: '40px' }}>
                <button className="btn-cancel" type="button" onClick={onCancel}>CANCELAR</button>
                <button className="btn-accept" type="submit">SIGUIENTE</button>
            </div>
        </form>
    );
};

export default EventualHuespedForm;