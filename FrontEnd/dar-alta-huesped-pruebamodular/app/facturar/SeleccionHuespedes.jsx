// src/components/SeleccionHuespedes.jsx
import React from 'react';
import itemsPendientes from'./FacturacionGeneral'
import '../styles/stylesCU4-5-15.css'
import './styles-facturacion.css'
function SeleccionHuespedes({ huespedes, onSelectResponsable }) {
    return (
        <div className="right-panel">
            <h3>HUESPEDES</h3>
            <p>Seleccione un responsable de pago</p>
            {huespedes.map(huesped => (
                <div key={huesped.id} className="huesped-card">
                    <span>{huesped.nombre}</span>
                    <span>{huesped.apellido}</span>
                    <span>DNI: {huesped.dni}</span>
                    <button className='btn-accept' onClick={() => onSelectResponsable(huesped)}>
                        ...
                    </button>
                </div>
                
            ))}
            <p>Items pendientes de facturar: {itemsPendientes.length}</p>
        </div>
    );
}

export default SeleccionHuespedes;