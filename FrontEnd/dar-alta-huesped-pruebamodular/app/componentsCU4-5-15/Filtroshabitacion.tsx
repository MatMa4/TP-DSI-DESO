'use client'; 
import React, { useState, useEffect } from 'react';
import { InputField } from './InputField'; 
import { ROOM_TYPES } from '../types/indexCU4-5-15'; 

interface FiltrosHabitacionProps {
    fechas: { desde: string, hasta: string };
    onChange: (e: React.ChangeEvent<any>) => void;
    onSearch: (tipo: string) => void; 
    onCancel: () => void;
    error?: string;
}

const FiltrosHabitacion: React.FC<FiltrosHabitacionProps> = ({ fechas, onChange, onSearch, onCancel, error }) => {
    const [selectedType, setSelectedType] = useState(ROOM_TYPES[0] || ''); 
    const allRoomTypes = ROOM_TYPES; 

    useEffect(() => {
        if (!selectedType && allRoomTypes.length > 0) {
            setSelectedType(allRoomTypes[0]);
        }
    }, [allRoomTypes, selectedType]);

    const handleAcceptClick = () => {
        if (selectedType) {
            onSearch(selectedType);
        }
    };
    
    // Verifica si las fechas son válidas para habilitar el botón
    const isSearchValid = fechas.desde && fechas.hasta && !error && selectedType;

    return (
        <div className="filtros-habitacion-box">
            {/* 1. Selectores de Fechas */}
            <div className="date-selectors" style={{ display: 'flex', gap: '20px', marginBottom: '15px', marginTop:'20px' }}>
                <div className="date-input-card">    
                    <InputField
                        label="Desde"
                        name="desde"
                        value={fechas.desde}
                        onChange={onChange}
                        type="date"
                        isRequired={true}
                        error={error}
                    />
                </div>
                <div className="date-input-card">  
                    <InputField
                        label="Hasta"
                        name="hasta"
                        value={fechas.hasta}
                        onChange={onChange}
                        type="date"
                        isRequired={true}
                    />
                </div>  
            </div>

            {/*Desplegable y Botón en línea */}
            <div className="room-type-search-line" style={{ display: 'flex', alignItems: 'stretch',  gap:'15px', marginBottom: '20px' }}>
                
                <div style={{ display: 'flex', alignItems: 'center', gap: '0' }}>
                    <label className="type-label-button">Tipo</label>
                    <select
                        className="btn-accept" 
                        value={selectedType}
                        onChange={(e) => setSelectedType(e.target.value)}
                    >
                        {allRoomTypes.map(type => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>
                    
                </div>
                <button 
                        className="btn-accept" 
                        type="button" 
                        onClick={handleAcceptClick}
                        disabled={!isSearchValid}
                        >
                        Mostrar Disponibilidad
                </button>
                
            </div>
        </div>
    );
};

export default FiltrosHabitacion;