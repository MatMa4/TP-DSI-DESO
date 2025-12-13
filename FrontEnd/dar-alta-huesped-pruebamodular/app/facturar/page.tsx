'use client';
import React, { useState, useMemo } from 'react';
import { useRouter } from 'next/navigation';

// Componentes Reutilizables y específicos (Ajustar rutas según tu proyecto)
import SeleccionHuespedes from './SeleccionHuespedes'; 
import FacturacionGeneral from './FacturacionGeneral';
import DetalleFacturaModal from './DetalleFacturaModal';
import { InputField } from '../componentsCU4-5-15/InputField'; // Usaremos InputField si está disponible
import ModalError from '../componentsCU4-5-15/ModalError'; 
import '../styles/stylesFacturar.css'; // Asegúrate de crear este archivo CSS
import {OcupacionDTO,HuespedDTO,ItemConsumoDTO, ITEMS_CONSUMO_MOCK} from './interfaces';

// --- STAGES ---
enum EtapaFacturacion {
    BUSQUEDA = 'BUSQUEDA',
    FACTURACION = 'FACTURACION'
}

// --- INTERFACES ---

interface SearchFormData {
    numeroHabitacion: string;
    horaSalida: string; 
}

// --- COMPONENTE PRINCIPAL ---
export default function GenerarFactura() {
    
    const [etapa, setEtapa] = useState<'BUSQUEDA'>('BUSQUEDA');
    // Datos de búsqueda
    const [formData, setFormData] = useState<SearchFormData>({
        numeroHabitacion: '',
        horaSalida: '10:00',
    });
    // Resultados de la búsqueda inicial
    const [searchResults, setSearchResults] = useState<HuespedDTO[]>([]);
    // El responsable seleccionado
    const [responsableSeleccionado, setResponsableSeleccionado] = useState<HuespedDTO | null>(null);
    // Estados de UI
    const [isLoading, setIsLoading] = useState(false);
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const router = useRouter();
    //
    const [busquedaRealizada, setBusquedaRealizada] = useState(false);
    
    //consumos
    const [itemsConsumo, setItemsConsumo] = useState<ItemConsumoDTO[]>(ITEMS_CONSUMO_MOCK);
    const [showDetalleModal, setShowDetalleModal] = useState(false);
    const itemsPendientes = itemsConsumo.filter(item => !item.facturado);


    // --- MANEJADORES DE ESTADO ---

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
        // Limpieza básica de errores al escribir
        setErrorMessage('');
    };

    const validateForm = (): boolean => {
        if (!formData.numeroHabitacion.trim()) {
            setErrorMessage('Debe ingresar el número de habitación.');
            setShowErrorModal(true);
            return false;
        }
        return true;
    };


    // --- LÓGICA DE BÚSQUEDA (HANDLES)---

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;

        setIsLoading(true);
        setBusquedaRealizada(false);
        setSearchResults([]);
        
        const { numeroHabitacion, horaSalida } = formData;
        const numHabitacionLimpio = parseInt(numeroHabitacion);
        console.log("Número enviado:", typeof numHabitacionLimpio, numHabitacionLimpio);

        if (isNaN(numHabitacionLimpio)) {
            setErrorMessage('El número de habitación debe ser un valor numérico válido.');
            setShowErrorModal(true);
        return;
        }
        const horaSalidaCompleta =`${horaSalida}:00`;
        // Endpoint
        const BASE_URL = 'http://localhost:8080';
        const url = `${BASE_URL}/ocupacion?numero=${numHabitacionLimpio}&hora=${horaSalidaCompleta}`;
        console.log("URL de búsqueda:", url);  
        
        try {

            const response = await fetch(url);
            
            if (!response.ok) {
                throw new Error(`Error ${response.status}: No se pudo buscar la ocupación.`);
            }
            
            const data: OcupacionDTO = await response.json();
            
            if (data && Array.isArray(data.huespedes) && data.huespedes.length > 0) {
        
        // Aquí debes mapear data.huespedes a la interfaz que espera el estado
                const huespedesMapeados:HuespedDTO[] = data.huespedes.map(huesped => ({
                    numeroDocumento: huesped.numeroDocumento,
                    tipoDocumento: huesped.tipoDocumento,
                    nombre: huesped.nombre,
                    apellido: huesped.apellido,
                    fechaNacimiento: huesped.fechaNacimiento,
                    telefono: huesped.telefono,
                    email: huesped.email,
                    ocupacion: huesped.ocupacion,
                    nacionalidad: huesped.nacionalidad,
                    cuit: huesped.cuit,
                    posicionIVA: huesped.posicionIVA,
                    alojado: huesped.alojado,
                    direccionHuesped: huesped.direccionHuesped,
                    
                }));

            setSearchResults(huespedesMapeados); 
            setBusquedaRealizada(true);
            
            } else {
                // Manejo de caso vacío o no encontrado
                setErrorMessage("No se encontraron huéspedes en esa habitación...");
                setShowErrorModal(true);
                setSearchResults([]); 
                setBusquedaRealizada(true); 
            }

        } catch (error) {
            setErrorMessage(`Error de conexión o API: ${error.message}`);
            setShowErrorModal(true);
        } finally {
            setIsLoading(false);
        }
    };
    
    const handleCancelar = () => {
        // En una aplicación real, aquí podrías volver al menú principal
        router.push('/');
    };

    const handleSeleccionarResponsable = (huesped: HuespedDTO) => {
        console.log("Responsable seleccionado. Abriendo modal de detalle.");
        setResponsableSeleccionado(huesped);
        // Aquí podrías hacer un fetch real de los consumos si no los cargaste antes
        setShowDetalleModal(true);
    };
    const handleVolverABusqueda = () => {
        setResponsableSeleccionado(null);
        setEtapa('BUSQUEDA');
    };
    
    const handleCerrarModal = () => {
    setShowDetalleModal(false);
    setResponsableSeleccionado(null); 
    };

    const handleGenerarFactura = (itemsSeleccionadosIds: number[]) => {
        if (!responsableSeleccionado) return;

        // Lógica de actualización de estado (simula el guardado)
        const nuevosItems = itemsConsumo.map(item => {
            if (itemsSeleccionadosIds.includes(item.id)) {
                return { 
                    ...item, 
                    facturado: true, 
                    responsable: responsableSeleccionado.numeroDocumento, // Usamos DNI como ID
                };
            }
            return item;
        });

        setItemsConsumo(nuevosItems);
        handleCerrarModal();
    };
    /*const itemsPendientes = useMemo(() => 
        itemsConsumo.filter(item => !item.facturado)
        , [itemsConsumo]
    );
    */
    // --- RENDERIZADO (UI) ---

    return (
        <main className="main-container-facturar">
            <div className="facturar-layout">
                {/* PANEL IZQUIERDO (BÚSQUEDA) */}
                <div className="left-pane-facturar">
                    <div className="header-title-box">
                        <h1 className="main_title">Generar Factura</h1>
                    </div>

                    <form onSubmit={handleSearch} className="form-container-facturar">
                        <InputField 
                            label="N° de Habitación" 
                            name="numeroHabitacion" 
                            value={formData.numeroHabitacion} 
                            onChange={(e) => setFormData({...formData, numeroHabitacion: e.target.value})} 
                            type="number"
                        />
                        
                        {/* Campo para la hora de salida */}
                        <InputField 
                            label="Hora de Salida" 
                            name="horaSalida" 
                            value={formData.horaSalida} 
                            onChange={(e) => setFormData({...formData, horaSalida: e.target.value})} 
                            type="time"
                        />
                        
                        <div className="form-actions-facturar">
                            <button type="button" className="btn-cancel" onClick={() => router.push('/')}>
                                Cancelar
                            </button>
                            <button type="submit" className="btn-search" disabled={isLoading}>
                                {isLoading ? 'Buscando...' : 'Buscar'}
                            </button>
                        </div>
                    </form>
                </div>

                {/* PANEL DERECHO (RESULTADOS) */}
                <div className="right-pane-facturar">
                    <div className="results-box-facturar">
                        <SeleccionHuespedes 
                            huespedes={searchResults} 
                            onSelect={handleSeleccionarResponsable} 
                        />
                    </div>
                </div>
            </div>
            <ModalError
                show={showErrorModal}
                message={errorMessage}
                onClose={() => setShowErrorModal(false)}
            />
            {showDetalleModal && responsableSeleccionado && (
            <DetalleFacturaModal
                show={showDetalleModal}
                onClose={handleCerrarModal}
                habitacionNumero={formData.numeroHabitacion}
                responsable={responsableSeleccionado}
                itemsPendientes={itemsPendientes} // La lista de ítems sin facturar
                onConfirmFactura={handleGenerarFactura}
            />
            
            )}
        </main>
    );
}