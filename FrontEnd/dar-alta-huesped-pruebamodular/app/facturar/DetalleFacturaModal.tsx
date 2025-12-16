import React, { useState, useMemo, useEffect } from 'react';
import {HuespedDTO,ItemConsumoDTO} from './interfaces';

interface DetalleFacturaModalProps {
    show: boolean;
    onClose: () => void;
    responsable: HuespedDTO; 
    itemsPendientes: ItemConsumoDTO[]; 
    onConfirmFactura: (itemConsumoIds: number[], incluirEstadia: boolean) => void;
    precioEstadia: number;
    estadiaYaFacturada: boolean;
}
interface ItemConsumoLocal extends ItemConsumoDTO {
    seleccionado: boolean; 
}
const IVA_PERCENTAGE = 0.30; 

const DetalleFacturaModal: React.FC<DetalleFacturaModalProps> = ({ 
    show,
    onClose,
    precioEstadia,
    estadiaYaFacturada,
    responsable, 
    itemsPendientes,
    onConfirmFactura, }) => {
    
    const [isEstadiaSelected, setIsEstadiaSelected] = useState(!estadiaYaFacturada);
    const [itemsSeleccionados, setItemsSeleccionados] = useState<ItemConsumoLocal[]>(() => {

    if (!Array.isArray(itemsPendientes)) {
        return [];
    }
    // Mapeo itemsPendientes
    return itemsPendientes.map(item => ({ 
        ...item, 
        seleccionado: true
    }));
    
});
    useEffect(() => {
        if (estadiaYaFacturada) {
            setIsEstadiaSelected(false);
        }
    }, [estadiaYaFacturada]);
    
    // FUNCIÓN PARA CALCULAR EL TOTAL
    const totalConsumo = useMemo(() => {
        return itemsSeleccionados
            .filter(item => item.seleccionado)
            .reduce((sum, item) => sum + item.monto, 0);
    }, [itemsSeleccionados]);
    
    // Cálculo final
    const precioEstadiaCalculado = (isEstadiaSelected && !estadiaYaFacturada) ? precioEstadia : 0;
    const subtotal = totalConsumo + precioEstadiaCalculado;
    const iva = subtotal * IVA_PERCENTAGE;
    const totalFinal = subtotal + iva;

    // Maneja el toggle de selección de un ítem
    const handleToggleItem = (id:number) => {
        setItemsSeleccionados(prevItems => 
            prevItems.map(item => 
                item.idConsumo === id ? { ...item, seleccionado: !item.seleccionado } : item
            )
        );
    };
    const handleToggleEstadia = () => {
        setIsEstadiaSelected(prev => !prev);
    };

    const handleConfirm = () => {
        const idsAFacturar = itemsSeleccionados
            .filter(item => item.seleccionado)
            .map(item => item.idConsumo);
        
        const vaAFacturarEstadia = isEstadiaSelected && !estadiaYaFacturada;
        const hayAlgoSeleccionado = idsAFacturar.length > 0 || vaAFacturarEstadia;
            
        if (!hayAlgoSeleccionado) {
        alert("Debe seleccionar al menos un ítem para facturar.");
        return; 
    }
        onConfirmFactura(idsAFacturar,isEstadiaSelected);
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
                        {!estadiaYaFacturada && (
                        <tr>
                            <td>Estadía:</td>
                            <td>$ {precioEstadia.toLocaleString('es-AR')}</td>
                            <td>
                                <input 
                                    type="checkbox"
                                    checked={isEstadiaSelected}
                                    onChange={handleToggleEstadia}
                                    style={{ marginRight: '10px' }}
                                    />
                            </td>
                        </tr> 
                        )}          
                        {Array.isArray(itemsPendientes) && itemsPendientes.length > 0 ? (
                        itemsSeleccionados.map(item => (
                            <tr key={item.idConsumo}>
                                <td>{item.detalle}</td>
                                <td>$ {item.monto.toLocaleString('es-AR')}</td>
                                <td>
                                    <input 
                                        type="checkbox" 
                                        checked={item.seleccionado} 
                                        onChange={() => handleToggleItem(item.idConsumo)}
                                        style={{ marginRight: '10px' }}
                                    />
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td>
                                <p>No hay consumos pendientes para facturar.</p>
                            </td>
                        </tr>
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
                    <button 
                        className="btn-accept" 
                        onClick={handleConfirm}
                        disabled={totalFinal === 0}
                        style={{ opacity: totalFinal === 0 ? 0.5 : 1, cursor: totalFinal === 0 ? 'not-allowed' : 'pointer' }}
                    >
                        ACEPTAR
                    </button>
                </div>
            </div>
        </div>
    );
};

export default DetalleFacturaModal;