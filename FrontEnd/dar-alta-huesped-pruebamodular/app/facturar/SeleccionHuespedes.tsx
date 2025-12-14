// src/components/SeleccionHuespedes.jsx
import React from 'react';
import '../styles/stylesCU4-5-15.css'
import './styles-facturacion.css'
import {OcupacionDTO,HuespedDTO,ItemConsumoDTO, ITEMS_CONSUMO_MOCK} from './interfaces';

interface SeleccionHuespedesProps {
    huespedes: HuespedDTO[]; // Esto le dice que 'huespedes' es un arreglo de HuespedDTO
    onSelect: (huesped: HuespedDTO) => void; // Esto le dice que es una función que recibe un HuespedDTO
}
const SeleccionHuespedes: React.FC<SeleccionHuespedesProps> = ({ huespedes, onSelect }) => {
    if (!huespedes || huespedes.length === 0) {
        return <p style={{textAlign:'center',color:'grey'}}>No se han encontrado huéspedes o no se ha realizado búsqueda.</p>;
    }
    return (
        <div className="right-panel">
            <h3>HUESPEDES</h3>
            <p>Seleccione un responsable de pago</p>
            {huespedes.map(huesped => (
                <div key={huesped.numeroDocumento} className="huesped-card">
                    <span>{huesped.nombre}</span>
                    <span>{huesped.apellido}</span>
                    <span>DNI: {huesped.numeroDocumento}</span>
                    <button className='btn-accept' onClick={() => onSelect(huesped)} >
                        ...
                    </button>
                </div>
                
                
            ))}
            <div style={{display:'flex'}}>
                <button className='btn-accept'>
                otro
                </button>
            </div>
        </div>
    );
}

export default SeleccionHuespedes;