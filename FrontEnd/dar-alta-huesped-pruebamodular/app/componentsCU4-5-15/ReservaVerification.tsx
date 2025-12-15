'use client'; 
import React from 'react'; 
import { SelectedReservation } from '../types/indexCU4-5-15'; 

interface ReservaVerificationProps {
    show: boolean;
    reservations: SelectedReservation[];
    onAccept: () => void;
    onReject: () => void;
    onCancel: () => void;
}

const ReservaVerification: React.FC<ReservaVerificationProps> = ({ show, reservations, onAccept, onReject, onCancel }) => {
    
    if (!show) return null;

    return (

        <div className="modal-overlay"> 
            
            <div className="modal" style={{ width: '700px', maxWidth: '90%', padding: '25px' }}>
                
                <h3 style={{ fontStyle:'bold',marginBottom: '15px', color: '#022E66' }}>Resumen de Ocupación</h3>
                <p>Por favor, verifique el resumen de las habitaciones y fechas seleccionadas antes de continuar:</p>                
                <div style={{ margin: '20px 0', border: '1px solid #ccc', padding: '15px', maxHeight: '300px', overflowY: 'auto', backgroundColor: '#D9D9D9', borderRadius: '4px' }}>
                    {reservations.map((res: SelectedReservation, index: number) => (
                        <div key={index} style={{ marginBottom: '10px', padding: '10px', borderBottom: '1px dotted #eee', textAlign: 'left' }}>
                            <strong style={{ display: 'block' }}>Habitación: {res.roomId} ({res.type})</strong>
                            <ul>
                                <li><span style={{ fontWeight: 'bold' }}>Ingreso:</span> {res.fechaInicio} (12:00hs)</li>
                                <li><span style={{ fontWeight: 'bold' }}>Egreso:</span> {res.fechaFin} (10:00hs)</li>
                            </ul>
                        </div>
                    ))}
                </div>

                <div className="modal-buttons" style={{ justifyContent: 'space-between' }}>
                    
                    <button className="btn-cancel" onClick={onCancel}>CANCELAR</button> 
                    
                    <div style={{ display: 'flex', gap: '10px' }}>
                        <button className="btn-cancel" onClick={onReject}>RECHAZAR</button> 
                        
                        <button className="btn-accept" onClick={onAccept}>ACEPTAR</button>
                    </div>
                </div>
            </div> 
        </div> 
    );
};

export default ReservaVerification;