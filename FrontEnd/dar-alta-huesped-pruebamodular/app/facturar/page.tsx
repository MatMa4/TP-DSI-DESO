'use client';
import React, { useState, useMemo } from 'react';
import { useRouter } from 'next/navigation';

// Componentes Reutilizables y específicos (Ajustar rutas según tu proyecto)
import { InputField } from '../componentsCU4-5-15/InputField'; // Usaremos InputField si está disponible
import ModalError from '../componentsCU4-5-15/ModalError'; 
import '../styles/stylesFacturar.css'; // Asegúrate de crear este archivo CSS


// --- INTERFACES ---

interface SearchFormData {
    numeroHabitacion: string;
    horaSalida: string; 
}

interface HuespedFacturacionDTO {
    id: number;
    nombre: string;
    apellido: string;
    dni: string;
}


// --- COMPONENTE PRINCIPAL ---
export default function GenerarFactura() {
    const [formData, setFormData] = useState<SearchFormData>({
        numeroHabitacion: '',
        horaSalida: '10:00',
    });
    
    const [searchResults, setSearchResults] = useState<HuespedFacturacionDTO[]>([]);
    const [busquedaRealizada, setBusquedaRealizada] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const router = useRouter();


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


    // --- LÓGICA DE BÚSQUEDA ---

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
        // Endpoints
        const BASE_URL = 'http://localhost:8080';
        const url = `${BASE_URL}/ocupacion?numero=${numHabitacionLimpio}&hora=${horaSalidaCompleta}`;
        console.log("URL de búsqueda:", url);  
        try {

            const response = await fetch(url);
            
            if (!response.ok) {
                throw new Error(`Error ${response.status}: No se pudo buscar la ocupación.`);
            }
            
            const data: HuespedFacturacionDTO[] = await response.json();
            
            if (!Array.isArray(data) || data.length === 0) {
                setErrorMessage("No se encontraron huéspedes en esa habitación con la hora de salida especificada.");
                setShowErrorModal(true);
            }
            
            setSearchResults(data);
            setBusquedaRealizada(true);

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

    const handleSeleccionarResponsable = (huesped: HuespedFacturacionDTO) => {
        // Lógica para avanzar al formulario de facturación
        // Por ahora, solo logueamos o navegamos
        console.log('Responsable seleccionado para facturar:', huesped);
        // router.push(`/facturar/detalle?huespedId=${huesped.id}`); // Ejemplo de navegación
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
                            onChange={handleChange} 
                            type="text"
                        />
                        
                        {/* Campo para la hora de salida */}
                        <InputField 
                            label="Hora de Salida" 
                            name="horaSalida" 
                            value={formData.horaSalida} 
                            onChange={handleChange} 
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
                        <h2 className="results-header">HUÉSPEDES</h2>
                        {busquedaRealizada && searchResults.length === 0 && (
                            <p style={{ textAlign: 'center', padding: '20px' }}>
                                No se encontraron huéspedes en esta habitación.
                            </p>
                        )}
                        
                        {busquedaRealizada && searchResults.length > 0 && (
                            <div className="huesped-list-container">
                                <p className='instruction-text'>Seleccione un responsable de pago</p>
                                {searchResults.map((huesped) => (
                                    <div 
                                        key={huesped.dni} 
                                        className="huesped-card-facturar"
                                        onClick={() => handleSeleccionarResponsable(huesped)}
                                    >
                                        <span>{huesped.nombre}</span>
                                        <span>{huesped.apellido}</span>
                                        <span>DNI</span>
                                        <span>{huesped.dni}</span>
                                        <button className="btn-select-huesped">➔</button>
                                    </div>
                                ))}
                                <button className="btn-otro-responsable">Otro</button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
            
            
            <ModalError
                show={showErrorModal}
                message={errorMessage}
                onClose={() => setShowErrorModal(false)}
            />
        </main>
    );
}