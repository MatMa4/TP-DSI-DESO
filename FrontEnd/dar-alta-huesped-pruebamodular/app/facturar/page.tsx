'use client';
import React, { useState} from 'react';
import { useRouter } from 'next/navigation';
import SeleccionHuespedes from './SeleccionHuespedes'; 
import DetalleFacturaModal from './DetalleFacturaModal';
import { InputField } from '../componentsCU4-5-15/InputField'; 
import ModalError from '../componentsCU4-5-15/ModalError';
import CuitInputModal from './CuitImputModal';
import RazonSocialConfirmModal from './RazonSocialConfirmModal';
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
    
    // Verificaciones de seguridad
    if (!responsableSeleccionado || !datosOcupacion) {
        console.error("Faltan datos de responsable o ocupación.");
        return;
    }

    // 1. OBTENER LOS CONSUMOS SELECCIONADOS COMPLETOS
    const consumosSeleccionados = itemsConsumo
        .filter(item => itemsConsumoIds.includes(item.idConsumo))
        .map(item => {
            const { seleccionado, id, ...consumoOriginal } = item as any; 
            return consumoOriginal; 
        });

    // 2. AÑADIR EL ÍTEM DE ESTADÍA (Si fue seleccionado)
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

    // 3. PREPARAR EL OBJETO HUESPED/RESPONSABLE
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
    //URL
    const endpoint = esResponsableEmpresa 
        ? '/generar/juridica' // Para empresas
        : '/generar/fisica';  // Para huéspedes personales

    const url = `${BASE_URL}/facturas${endpoint}`;

    // 4. PAYLOAD FINAL
    const payloadFactura = {
        idOcupacion: datosOcupacion.id, 
        listaConsumos: consumosSeleccionados, 
        ...responsablePayload,
    };

    // 5. ENVIAR AL BACK-END
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payloadFactura)
        });

        if (!response.ok) {
            // Manejar errores 400 u otros
            const errorText = await response.text();
            throw new Error(`Error ${response.status}: ${errorText}`);
        }

        setItemsConsumo(prevItems => 
            prevItems.map(item => {
                if (itemsConsumoIds.includes(item.idConsumo)) {
                    return { ...item, facturado: true };
                }
                return item;
            })

        );
        if (incluirEstadia) {
            setEstadiaFacturada(true);
        }
        
        alert("Factura creada con éxito!");
        handleCerrarModal();

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
                            <button type="button" className="btn-cancel" onClick={handleCancelar}>
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
                            onSelectOtro={handleSelectOtro}
                            itemsPendientesCount={itemsPendientes.length}
                        />
                    </div>
                </div>
            </div>

            {showCuitModal && (
            <CuitInputModal 
                show={showCuitModal}
                onClose={() => setShowCuitModal(false)} // Vuelve a la selección de huésped
                onNext={handleBuscarRazonSocial} // Llama a la simulación de búsqueda
            />
            )}

            {showRazonSocialModal && (
            <RazonSocialConfirmModal
                show={showRazonSocialModal}
                razonSocial={razonSocial}
                onAccept={handleConfirmarRazonSocial} // Pasa al DetalleFacturaModal
                onCancel={handleRechazarRazonSocial}  // Vuelve a CuitInputModal
            />
            )}

            {showDetalleModal && responsableSeleccionado && (
            <DetalleFacturaModal
                show={showDetalleModal}
                onClose={handleCerrarModal}
                responsable={responsableSeleccionado}
                itemsPendientes={itemsPendientes} // La lista de ítems sin facturar
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
            
        </main>
    );
}