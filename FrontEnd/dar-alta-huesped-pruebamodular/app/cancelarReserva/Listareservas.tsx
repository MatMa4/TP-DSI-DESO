import React from 'react';
import { ReservaDTO } from './interfaces'; 

interface ListaReservasProps {
    reservas: ReservaDTO[];
    selectedReservas: ReservaDTO[];
    onToggleReserva: (reserva: ReservaDTO) => void;
}

const ListaReservas: React.FC<ListaReservasProps> = ({ reservas, onToggleReserva,selectedReservas }) => {
    
    const isSelected = (id: number) => selectedReservas.some(r => r.idReserva === id);

    if (!Array.isArray(reservas) || reservas.length === 0) {
        return (
            <div style={{textAlign: 'center', marginTop: '50px', color: '#555'}}>
                <h3>LISTADO DE RESERVAS</h3>
                <p>No hay reservas para mostrar. Realice una búsqueda.</p>
            </div>
        );
    }

    return (
        <div className="lista-reservas-container">
            <h3 className="results-header">RESULTADOS DE BÚSQUEDA</h3>
            
            {reservas.map((reserva) => {
                const selected = isSelected(reserva.idReserva);
                return (
                    <div 
                        key={reserva.idReserva} 
                        className={`reserva-card ${selected ? 'selected' : ''}`}
                        onClick={() => onToggleReserva(reserva)}
                    >
                        {/* Checkbox */}
                        <input 
                            type="checkbox"
                            className="reserva-checkbox"
                            checked={selected}
                            readOnly 
                        />
                    <div className="reserva-info">
                        <strong>Reserva #{reserva.idReserva} - {reserva.tipoHabitacion} (Habitación. {reserva.habitacionNumero})</strong>
                        <span>Huésped: {reserva.nombre} {reserva.apellido}</span>
                        <span>Fecha: {reserva.fechaInicio} al {reserva.fechaFin}</span>
                    </div>
                </div>
                );
            })}    
        </div>
    );
};

export default ListaReservas;