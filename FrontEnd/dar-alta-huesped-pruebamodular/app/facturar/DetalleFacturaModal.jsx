// src/components/DetalleFacturaModal.jsx
import React, { useState, useMemo } from 'react';

const IVA_PERCENTAGE = 0.30; // 30%

function DetalleFacturaModal({ huesped, itemsPendientes, onClose, onFacturar }) {
    // Estado para manejar qué ítems se seleccionan para ESTA factura
    const [itemsSeleccionados, setItemsSeleccionados] = useState(
        itemsPendientes.map(item => ({ ...item, seleccionado: true })) // Por defecto, selecciona todos los pendientes
    );

    // FUNCIÓN PARA CALCULAR EL TOTAL
    const totalConsumo = useMemo(() => {
        return itemsSeleccionados
            .filter(item => item.seleccionado)
            .reduce((sum, item) => sum + item.valor, 0);
    }, [itemsSeleccionados]);
    
    // Cálculo final
    const subtotal = totalConsumo;
    const iva = subtotal * IVA_PERCENTAGE;
    const totalFinal = subtotal + iva;

    // Maneja el toggle de selección de un ítem
    const handleToggleItem = (id) => {
        setItemsSeleccionados(prevItems => 
            prevItems.map(item => 
                item.id === id ? { ...item, seleccionado: !item.seleccionado } : item
            )
        );
    };

    // Al presionar ACEPTAR
    const handleAceptar = () => {
        const idsAFacturar = itemsSeleccionados
            .filter(item => item.seleccionado)
            .map(item => item.id);
            
        if (idsAFacturar.length === 0) {
            alert("Debe seleccionar al menos un ítem para facturar.");
            return;
        }

        onFacturar(idsAFacturar); // Llama a la función de FacturacionGeneral
    };


    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h3 className="modal-title">{huesped.nombre} {huesped.apellido}</h3>
                
                <table className="items-table">
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Valor</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {itemsSeleccionados.map(item => (
                            <tr key={item.id}>
                                <td>{item.descripcion}</td>
                                <td>$ {item.valor.toLocaleString('es-AR')}</td>
                                <td>
                                    <input 
                                        type="checkbox" 
                                        checked={item.seleccionado} 
                                        onChange={() => handleToggleItem(item.id)}
                                    />
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                
                <div className="total-summary">
                    <p style={{ textAlign:'end'}}>Total: <strong>$ {totalFinal.toLocaleString('es-AR')}</strong></p>
                    <p style={{ textAlign: 'left'}}>IVA ({IVA_PERCENTAGE * 100}%): <strong>$ {iva.toLocaleString('es-AR')}</strong></p>
                </div>
                <p style={{ textAlign:'left'}}>Tipo de factura: A</p>
                <div className="modal-actions">
                    <button className="btn-cancel" onClick={onClose}>Atrás</button>
                    <button className="btn-accept" onClick={handleAceptar}>ACEPTAR</button>
                </div>
            </div>
        </div>
    );
}

export default DetalleFacturaModal;