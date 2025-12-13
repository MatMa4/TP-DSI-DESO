import React, { useState, useMemo } from 'react';
import { ITEMS_CONSUMO_MOCK, HUESPEDES_MOCK } from './consumos';
import DetalleFacturaModal from './DetalleFacturaModal';
import SeleccionHuespedes from './SeleccionHuespedes';
import './styles-facturacion.css'

function FacturacionGeneral() {
    // Estado de la lista de ítems de consumo de la habitación
    const [itemsConsumo, setItemsConsumo] = useState(ITEMS_CONSUMO_MOCK);
    
    // Estado para gestionar el modal
    const [modalVisible, setModalVisible] = useState(false);
    const [responsablePago, setResponsablePago] = useState(null);

    // 1. FILTRO DE ITEMS PENDIENTES
    // Los ítems que aún no tienen una factura asociada.
    const itemsPendientes = useMemo(() => 
        itemsConsumo.filter(item => !item.facturado)
    , [itemsConsumo]);

    // 2. FUNCIÓN PARA MANEJAR LA SELECCIÓN DEL RESPONSABLE
    const handleSeleccionarResponsable = (huesped) => {
        setResponsablePago(huesped);
        setModalVisible(true); // Abre el modal de detalle
    };

    // 3. FUNCIÓN PARA GENERAR LA FACTURA (Lógica principal)
    const handleGenerarFactura = (itemsSeleccionadosIds) => {
        if (!responsablePago) return;

        // Aquí iría la llamada a la API o lógica de backend
        console.log(`Generando factura para: ${responsablePago.nombre} ${responsablePago.apellido}`);
        console.log('Items facturados:', itemsSeleccionadosIds);
        
        // Actualiza el estado: marca los ítems como facturados y asigna el responsable
        const nuevosItems = itemsConsumo.map(item => {
            if (itemsSeleccionadosIds.includes(item.id)) {
                return { 
                    ...item, 
                    facturado: true, 
                    responsable: responsablePago.id 
                };
            }
            return item;
        });

        setItemsConsumo(nuevosItems);
        
        // Cierra el modal y limpia el responsable
        setModalVisible(false);
        setResponsablePago(null); 
    };

    // 4. CONTROL DE FLUJO: Revisar si la facturación ha terminado
    const facturacionCompleta = itemsPendientes.length === 0;

    if (facturacionCompleta) {
        return (
            <div className="flex-container">
                <h2>✅ Facturación de la habitación completada.</h2>
                <button className="btn-primary">Finalizar Proceso</button>
            </div>
        );
    }
    
    return (
        <div className="flex-container">
            {/* Componente que replica la interfaz de la izquierda (N° Habitación, Hora, Buscar) */}
            <div className="left-panel">
                <p>Items pendientes de facturar: {itemsPendientes.length}</p>
            </div>

            {/* Panel de selección de huéspedes (derecha) */}
            <SeleccionHuespedes 
                huespedes={HUESPEDES_MOCK} 
                onSelectResponsable={handleSeleccionarResponsable} 
            />

            {/* Modal que se muestra al seleccionar un responsable */}
            {modalVisible && responsablePago && (
                <DetalleFacturaModal
                    huesped={responsablePago}
                    itemsPendientes={itemsPendientes} // Solo mostramos los ítems que faltan
                    onClose={() => setModalVisible(false)} // Cierra el modal (Atrás)
                    onFacturar={handleGenerarFactura} // Envía los ítems seleccionados para facturar
                />
            )}
        </div>
    );
}

export default FacturacionGeneral;