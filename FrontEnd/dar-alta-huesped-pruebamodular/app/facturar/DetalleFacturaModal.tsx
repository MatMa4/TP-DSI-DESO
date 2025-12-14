// src/components/DetalleFacturaModal.jsx
import React, { useState, useMemo, useEffect } from 'react';
import {OcupacionDTO,HuespedDTO,ItemConsumoDTO} from './interfaces';
import './consumos'

interface DetalleFacturaModalProps {
    show: boolean;
    onClose: () => void;
    responsable: HuespedDTO; // Usa la interfaz de huésped ya definida
    itemsPendientes: ItemConsumoDTO[]; // La lista de consumos
    onConfirmFactura: (itemIds: number[]) => void;
}
const IVA_PERCENTAGE = 0.30; 

const DetalleFacturaModal: React.FC<DetalleFacturaModalProps> = ({ 
    show,
    onClose,
    responsable, 
    itemsPendientes, // <--- LISTA RECIBIDA
    onConfirmFactura, }) => {
    // Estado para manejar qué ítems se seleccionan para ESTA factura
    const [selectedItems, setSelectedItems] = useState(() => {
        // Aseguramos que itemsPendientes sea un array antes de mapear
        if (!Array.isArray(itemsPendientes)) return []; 
        return itemsPendientes.map(item => ({ ...item, seleccionado: true }));
    });
    
    const [itemsSeleccionados, setItemsSeleccionados] = useState<ItemConsumoDTO[]>(() => {

    if (!Array.isArray(itemsPendientes)) {
        return [];
    }
    // 2. Mapeo 'itemsPendientes'.
    return itemsPendientes.map(item => ({ 
        ...item, 
        seleccionado: true
    }));
    
});
    
    // FUNCIÓN PARA CALCULAR EL TOTAL
    const totalConsumo = useMemo(() => {
        return itemsSeleccionados
            .filter(item => item.seleccionado)
            .reduce((sum, item) => sum + item.monto, 0);
    }, [itemsSeleccionados]);
    
    // Cálculo final
    const subtotal = totalConsumo;
    const iva = subtotal * IVA_PERCENTAGE;
    const totalFinal = subtotal + iva;

    // Maneja el toggle de selección de un ítem
    const handleToggleItem = (id:number) => {
        setItemsSeleccionados(prevItems => 
            prevItems.map(item => 
                item.id === id ? { ...item, seleccionado: !item.seleccionado } : item
            )
        );
    };

    const handleConfirm = () => {
        const idsAFacturar = itemsSeleccionados
            .filter(item => item.seleccionado)
            .map(item => item.id);
            
        if (idsAFacturar.length === 0) {
            alert("Debe seleccionar al menos un ítem para facturar.");
            return;
        }
        onConfirmFactura(idsAFacturar);
    };

    if (!show) return null;
    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h3 className="modal-title">{responsable.nombre} {responsable.apellido}</h3>
                
                <table className="items-table">
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Valor</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>

                        {Array.isArray(itemsPendientes) && itemsPendientes.length > 0 ? (

                        itemsSeleccionados.map(item => (
                            <tr key={item.id}>
                                <td>{item.descripcion}</td>
                                <td>$ {item.monto.toLocaleString('es-AR')}</td>
                                <td>
                                    <input 
                                        type="checkbox" 
                                        checked={item.seleccionado} 
                                        onChange={() => handleToggleItem(item.id)}
                                    />
                                </td>
                            </tr>
                        ))
                    ) : (
                        <p>No hay consumos pendientes para facturar.</p>
                        )}
                    </tbody>
                </table>
                
                <div className="total-summary">
                    <p style={{ textAlign:'end'}}>Total: <strong>$ {totalFinal.toLocaleString('es-AR')}</strong></p>
                    <p style={{ textAlign: 'left'}}>IVA ({IVA_PERCENTAGE * 100}%): <strong>$ {iva.toLocaleString('es-AR')}</strong></p>
                </div>
                <p style={{ textAlign:'left'}}>Tipo de factura: A</p>
                <div className="modal-actions">
                    <button className="btn-cancel" onClick={onClose}>Atrás</button>
                    <button className="btn-accept" onClick={handleConfirm}>ACEPTAR</button>
                </div>
            </div>
        </div>
    );
};

export default DetalleFacturaModal;