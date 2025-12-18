'use client';
import React, { useState} from 'react';
import { useRouter } from 'next/navigation';
import SeleccionHuespedes from './SeleccionHuespedes'; 
import DetalleFacturaModal from './DetalleFacturaModal';
import { InputField } from '../componentsCU4-5-15/InputField'; 
import ModalError from '../componentsCU4-5-15/ModalError';
import CuitInputModal from './CuitImputModal';
import RazonSocialConfirmModal from './RazonSocialConfirmModal';
import ModalFin from './ModalExito';
import '../styles/stylesFacturar.css'; 
import {OcupacionDTO,HuespedDTO,ItemConsumoDTO,PersonaJuridicaDTO} from './interfaces';

// --- INTERFACES ---

interface SearchFormData {
    numeroHabitacion: string;
    horaSalida: string; 
}

// --- COMPONENTE PRINCIPAL ---
export default function GenerarFactura() {
    // Datos de búsqueda
    const [formData, setFormData] = useState<SearchFormData>({
        numeroHabitacion: '',
        horaSalida: '10:00',
    });
    // Resultados de la búsqueda inicial
    const [searchResults, setSearchResults] = useState<HuespedDTO[]>([]);
    const [datosOcupacion, setDatosOcupacion] = useState<OcupacionDTO | null>(null);
    // El responsable seleccionado
    const [responsableSeleccionado, setResponsableSeleccionado] = useState<HuespedDTO | null>(null);
    const [esResponsableEmpresa, setEsResponsableEmpresa] = useState(false);
    // Estados de UI
    const [isLoading, setIsLoading] = useState(false);
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const router = useRouter();
    const [busquedaRealizada, setBusquedaRealizada] = useState(false);
    const [showModalExito, setShowModalExito] = useState(false);
    
    //CONSUMOS
    const [itemsConsumo, setItemsConsumo] = useState<ItemConsumoDTO[]>([]);
    const [showDetalleModal, setShowDetalleModal] = useState(false);
    const itemsPendientes = itemsConsumo.filter(item => !item.facturado);
    const [estadiaFacturada, setEstadiaFacturada] = useState(false);

    //RAZON SOCIAL
    const [showCuitModal, setShowCuitModal] = useState(false);
    const [showRazonSocialModal, setShowRazonSocialModal] = useState(false);
    const [cuitIngresado, setCuitIngresado] = useState('');
    const [razonSocial, setRazonSocial] = useState('');
    const [empresaEncontrada, setEmpresaEncontrada] = useState<PersonaJuridicaDTO | null>(null);

    // --- MANEJADORES DE ESTADO ---

    const validateForm = (): boolean => {
        if (!formData.numeroHabitacion.trim()) {
            setErrorMessage('Debe ingresar el número de habitación.');
            setShowErrorModal(true);
            return false;
        }
        return true;
    };


    // --- LÓGICA DE BÚSQUEDA (HANDLES)---
    const BASE_URL = 'http://localhost:8080';

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;

        setIsLoading(true);
        setBusquedaRealizada(false);
        setSearchResults([]);
        setItemsConsumo([]);
        
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
        const url = `${BASE_URL}/ocupacion?numero=${numHabitacionLimpio}&hora=${horaSalidaCompleta}`;
        console.log("URL de búsqueda:", url);  
        
        try {

            const response = await fetch(url);
            
            if (!response.ok) {
                throw new Error(`Error ${response.status}: No se pudo buscar la ocupación.`);
            }
            
            const data: OcupacionDTO = await response.json();
            setDatosOcupacion(data);
            
            if (data && Array.isArray(data.huespedes) && data.huespedes.length > 0) {
        
                // Mapeo de huespedes
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
                // Mapeo de consumos
                if (Array.isArray(data.consumos)) {
                    setItemsConsumo(data.consumos);
                } else {
                    setItemsConsumo([]);
                }
                
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
        router.push('/menuCU1');
    };

    const handleSeleccionarResponsable = (huesped: HuespedDTO) => {
        setResponsableSeleccionado(huesped);
        setEsResponsableEmpresa(false);
        setShowDetalleModal(true);
    };

    const handleCerrarModal = () => {
    setShowDetalleModal(false);
    setResponsableSeleccionado(null); 
    };

    const handleGenerarFactura = async (itemsConsumoIds: number[], incluirEstadia: boolean) => {
    
    // Verificaciones 
    if (!responsableSeleccionado || !datosOcupacion) {
        console.error("Faltan datos de responsable o ocupación.");
        return;
    }

    const consumosSeleccionados = itemsConsumo
        .filter(item => itemsConsumoIds.includes(item.idConsumo))
        .map(item => {
            const { seleccionado, id, ...consumoOriginal } = item as any; 
            return consumoOriginal; 
        });
        /*
    if (incluirEstadia) {
        const itemEstadia: ItemConsumoDTO = {
            idConsumo: 0, 
            tipoServicio: "Alojamiento",
            detalle: `Estadía ${datosOcupacion.habitacion.tipoHabitacion} (${datosOcupacion.fechaInicio} a ${datosOcupacion.fechaFin})`,
            monto: datosOcupacion.precioTotal, 
            facturado: false,
        };
        consumosSeleccionados.push(itemEstadia);
    }
    */

    let responsablePayload: any;
    if (esResponsableEmpresa) {
        responsablePayload = {
            cuitResponsable: responsableSeleccionado.numeroDocumento 
        };
    } else {
        responsablePayload = {
            huesped:{
            numeroDocumento: responsableSeleccionado.numeroDocumento,
            tipoDocumento: responsableSeleccionado.tipoDocumento || 'DNI', 
            apellido: responsableSeleccionado.apellido,
            nombre: responsableSeleccionado.nombre,
            fechaNacimiento: responsableSeleccionado.fechaNacimiento,
            telefono: responsableSeleccionado.telefono,
            email: responsableSeleccionado.email,
            ocupacion: responsableSeleccionado.ocupacion,
            nacionalidad: responsableSeleccionado.nacionalidad,
            cuit: responsableSeleccionado.cuit,
            posicionIVA: responsableSeleccionado.posicionIVA,
            alojado: responsableSeleccionado.alojado,
            direccionHuesped: responsableSeleccionado.direccionHuesped
            }
        }    
    };

    const endpoint = esResponsableEmpresa 
        ? '/generar/juridica' 
        : '/generar/fisica'; 

    const url = `${BASE_URL}/facturas${endpoint}`;

    const payloadFactura = {
        idOcupacion: datosOcupacion.id, 
        listaConsumos: consumosSeleccionados, 
        ...responsablePayload,
    };
    if (!incluirEstadia){
        payloadFactura.idOcupacion = 0;
    }
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payloadFactura)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error ${response.status}: ${errorText}`);
        }

        const nuevosItems = itemsConsumo.map(item => {
            if (itemsConsumoIds.includes(item.idConsumo)) {
                return { ...item, facturado: true };
            }
            return item;
        });

        const nuevaEstadiaFacturada = estadiaFacturada || incluirEstadia;

        setItemsConsumo(nuevosItems);
        if (incluirEstadia) {
            setEstadiaFacturada(true);
        }

        const quedanConsumosPendientes = nuevosItems.some(item => !item.facturado);
        

        const faltaEstadia = !nuevaEstadiaFacturada;

        handleCerrarModal(); 

        if (!quedanConsumosPendientes && !faltaEstadia) {
            setShowModalExito(true); 
        } else {
            alert("Factura creada con éxito. Aún quedan ítems pendientes.");
        }

    } catch (error) {
        setErrorMessage("No se pudo generar la factura: " + (error instanceof Error ? error.message : 'Error desconocido'));
        setShowErrorModal(true);
    }
};

    const handleSelectOtro = () => {
    setShowCuitModal(true); 
    };

    const handleBuscarRazonSocial = async (cuit: string) => {
        setCuitIngresado(cuit);
        setIsLoading(true); 

        const url = `${BASE_URL}/responsable-pago/juridica?cuit=${cuit}`; 

        try {
            const response = await fetch(url);

            if (!response.ok) {
                // Si da 404, significa que el cliente no existe
                if (response.status === 404) {
                    throw new Error("Cliente no encontrado. Verifique el CUIT.");
                }
                throw new Error("Error al buscar el cliente.");
            }

            const data: PersonaJuridicaDTO = await response.json();
            
            // ÉXITO 
            setEmpresaEncontrada(data);
            setRazonSocial(data.razonSocial); 
            
            setShowCuitModal(false);
            setShowRazonSocialModal(true);

        } catch (error) {
            setErrorMessage(error instanceof Error ? error.message : 'Error desconocido');
            setShowErrorModal(true);
        } finally {
            setIsLoading(false);
        }
    };

    const handleConfirmarRazonSocial = () => {
        if (!empresaEncontrada) return;

        const responsableEmpresa: HuespedDTO = {
            nombre: empresaEncontrada.razonSocial, 
            apellido: '.', 
            numeroDocumento: empresaEncontrada.cuit,
            tipoDocumento: 'CUIT', 
            telefono: empresaEncontrada.telefono || '',
            fechaNacimiento: '',
            email: '',
            ocupacion: '',
            nacionalidad: '',
            cuit: empresaEncontrada.cuit,
            posicionIVA: '',
            alojado: true,
            direccionHuesped: empresaEncontrada.direccion,
        };
        setResponsableSeleccionado(responsableEmpresa);
        setEsResponsableEmpresa(true);
        setShowRazonSocialModal(false);
        setShowDetalleModal(true);
    };

    const handleRechazarRazonSocial = () => {
        setCuitIngresado('');
        setRazonSocial('');
        setShowRazonSocialModal(false);
        setShowCuitModal(true);
    };

    const handleFinalizarTodo = () => {
    setShowModalExito(false);
    router.push('/menuCU1'); 
    };

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
                        
                        <InputField 
                            label="Hora de Salida" 
                            name="horaSalida" 
                            value={formData.horaSalida} 
                            onChange={(e) => setFormData({...formData, horaSalida: e.target.value})} 
                            type="time"
                        />
                        
                        <div className="form-actions-facturar">
                            <button type="button" className="btn-cancel" onClick={handleCancelar}>
                                Cancelar
                            </button>
                            <button type="submit" className="btn-search" disabled={isLoading}>
                                {isLoading ? 'Buscando...' : 'Buscar'}
                            </button>
                        </div>
                    </form>
                </div>

                {/* PANEL DERECHO */}
                <div className="right-pane-facturar">
                    <div className="results-box-facturar">
                        <SeleccionHuespedes 
                            huespedes={searchResults} 
                            onSelect={handleSeleccionarResponsable} 
                            onSelectOtro={handleSelectOtro}
                            itemsPendientesCount={itemsPendientes.length}
                        />
                    </div>
                </div>
            </div>

            {showCuitModal && (
            <CuitInputModal 
                show={showCuitModal}
                onClose={() => setShowCuitModal(false)} 
                onNext={handleBuscarRazonSocial} 
            />
            )}

            {showRazonSocialModal && (
            <RazonSocialConfirmModal
                show={showRazonSocialModal}
                razonSocial={razonSocial}
                onAccept={handleConfirmarRazonSocial}
                onCancel={handleRechazarRazonSocial}
            />
            )}

            {showDetalleModal && responsableSeleccionado && (
            <DetalleFacturaModal
                show={showDetalleModal}
                onClose={handleCerrarModal}
                responsable={responsableSeleccionado}
                itemsPendientes={itemsPendientes}
                onConfirmFactura={handleGenerarFactura}
                precioEstadia={datosOcupacion.precioTotal}
                estadiaYaFacturada={estadiaFacturada}
            />
            )}

            <ModalError
                show={showErrorModal}
                message={errorMessage}
                onClose={() => setShowErrorModal(false)}
            />
            <ModalFin 
                show={showModalExito}
                message="Se han facturado todos los conceptos de la habitación. Presione cualquier tecla para continuar..."
                onClose={handleFinalizarTodo}   
                onConfirm={handleFinalizarTodo} 
            />
            
        </main>
    );
}