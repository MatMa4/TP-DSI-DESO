'use client';
import React, { useState, useMemo } from 'react';
import { InputField } from './InputField';
import DocumentoField from '../components/DocumentoFieldCU2';
import ModalError from './ModalError';
import { validateBuscarForm } from '../buscarHuesped/ValidacionDatosCU2'; 
import ModalConfirmacion from '../componentsCU4-5-15/ModalConfirmacion';
import { HuespedDTO } from '../types/indexCU4-5-15'; 
import { useRouter } from 'next/navigation';
import '../styles/stylesCU15_busqueda.css'; 



// Props que recibe del componente padre (ocupar/page.tsx)
interface HuespedSearchAndSelectProps {
  onSelectionSubmit: (selectedHuespedes: HuespedDTO[]) => void;
  onCancel: () => void;
}

// Interfaz para el estado de ordenamiento
interface SortConfig {
    key: keyof HuespedDTO;
    direction: 'asc' | 'desc';
}


const HuespedSearchAndSelect: React.FC<HuespedSearchAndSelectProps> = ({ onSelectionSubmit, onCancel }) => {
    
   const [huespedData, setHuespedData] = useState<HuespedDTO>({
    nombre: '',
    apellido: '',
    tipoDocumento: '', 
    numeroDocumento: '',
    fechaNacimiento: '', 
    telefono: '', 
    email: '',
    ocupacion: '',
    nacionalidad: '',
    cuit: '',
    posicionIVA: 'Consumidor final',
    alojado: true,
    direccionHuesped: { 
        calle: '', numero: '', departamento: '', piso: '', 
        codigo: '', localidad: '', provincia: '', pais: ''
    }
    });
    const router = useRouter();
    const [errors, setErrors] = useState<Record<string, string>>({});
    const [isLoading, setIsLoading] = useState(false);
    const [busquedaRealizada, setBusquedaRealizada] = useState(false);
    
    // --- ESTADOS ADAPTADOS PARA SELECCIÓN MÚLTIPLE (Panel Derecho) ---
    const [searchResults, setSearchResults] = useState<HuespedDTO[]>([]); // Resultados de la búsqueda
    const [selectedHuespedes, setSelectedHuespedes] = useState<Set<string>>(new Set()); // Guarda NroDocumento de los seleccionados
    const [sortConfig, setSortConfig] = useState<SortConfig | null>(null); 
    const [seleccionadoId, setSeleccionadoId] = useState<string | null>(null);
    
    // --- ESTADOS DE MODAL ---
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [showCancelModal, setShowCancelModal] = useState(false);
    
    // --- FUNCIONES COPIADAS DE BuscarHuesped ---
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        // Forzar mayúsculas en Nombre y Apellido 
        const valorFinal = (name === 'nombre' || name === 'apellido') ?
            value.toUpperCase() : value;

        setHuespedData(prev => ({ ...prev, [name]: valorFinal }));
        
        // Limpiar error al escribir
        if (errors[name]) {
            setErrors(prev => {
                const newErrors = { ...prev };
                delete newErrors[name];
                return newErrors;
            });
        }
    };

    const validateBuscarForm = (data: HuespedDTO): Record<string, string> => {
        const validationErrors: Record<string, string> = {};
        return validationErrors;
    };
const handleConfirmCancel = () => {
    setShowCancelModal(false);
    // Redirección infalible al menú principal
    router.push('/');
    };


    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        
        const validationErrors = validateBuscarForm(huespedData);
        if (Object.keys(validationErrors).length > 0) {
            // Usamos un modal global en lugar de errores bajo el input para la validación general
            setErrorMessage(validationErrors.general || 'Datos inválidos en el formulario.');
            setShowErrorModal(true);
            return;
        }

        setIsLoading(true);
        setSelectedHuespedes(new Set()); // Limpiar selección previa
        setSearchResults([]); 
        setSortConfig(null); 

        try {
            // 2. Construir URL dinámica
            const params = new URLSearchParams();
            if (huespedData.nombre.trim()) params.append('nombre', huespedData.nombre.trim());
            if (huespedData.apellido.trim()) params.append('apellido', huespedData.apellido.trim());
            if (huespedData.numeroDocumento.trim()) params.append('numero', huespedData.numeroDocumento.trim());
            if (huespedData.tipoDocumento.trim()) params.append('tipo', huespedData.tipoDocumento);

            const queryString = params.toString();
            // CRÍTICO: Asegúrate que esta URL sea la correcta para buscar huéspedes
            const urlFinal = `http://localhost:8080/huespedes/buscar${queryString ? `?${queryString}` : ''}`;
            
            console.log('Buscando Huéspedes en:', urlFinal);

            const response = await fetch(urlFinal, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
            });
            
            if (!response.ok) throw new Error(`Error ${response.status}: Fallo al buscar huéspedes.`); 

            const data = await response.json();
            
            if (!Array.isArray(data) || data.length === 0) {
                setErrorMessage("No se encontraron resultados que coincidan con los criterios.");
                setShowErrorModal(true);
            }

            setSearchResults(data);
            setBusquedaRealizada(true);
        } catch (error) {
            console.error("Error:", error); 
            setErrorMessage("Error de conexión con el Backend al buscar. Revise la consola.");
            setShowErrorModal(true);
        } finally {
            setIsLoading(false); 
        }
    };
    
    // --- LÓGICA DE ORDENAMIENTO (Copiada de BuscarHuesped) ---
    const handleSort = (key: keyof HuespedDTO) => {
        let direction: 'asc' | 'desc' = 'asc'; 
        if (sortConfig && sortConfig.key === key && sortConfig.direction === 'asc') {
            direction = 'desc'; 
        }
        setSortConfig({ key, direction });
    };

    const resultadosOrdenados = useMemo(() => {
        let sortedData = [...searchResults];
        if (sortConfig !== null) {
            sortedData.sort((a, b) => {
                const key = sortConfig.key as keyof HuespedDTO;
                if (a[key] < b[key]) {
                    return sortConfig.direction === 'asc' ? -1 : 1;
                }
                if (a[key] > b[key]) {
                    return sortConfig.direction === 'asc' ? 1 : -1; 
                }
                return 0;
            });
        }
        return sortedData;
    }, [searchResults, sortConfig]);
    
    const getSortIcon = (key: keyof HuespedDTO) => {
        if (!sortConfig || sortConfig.key !== key) return null; 
        return sortConfig.direction === 'asc' ? ' ▲' : ' ▼';
    };
    // --------------------------------------------------------

    // --- LÓGICA DE SELECCIÓN MÚLTIPLE (Adaptada al CU15) ---
    const toggleHuespedSelection = (nroDocumento: string) => {
        setSelectedHuespedes(prev => {
            const next = new Set(prev);
            if (next.has(nroDocumento)) {
                next.delete(nroDocumento);
            } else {
                next.add(nroDocumento);
            }
            return next;
        });
    };
    
    const handleSubmitSelection = () => {
        if (selectedHuespedes.size === 0) {
        setErrorMessage('Debe seleccionar al menos un huésped para asociar a la ocupación.');
        setShowErrorModal(true);
        return;
    }

    // Filtra los resultados para obtener los DTOs completos de los huéspedes seleccionados
    const finalSelection = searchResults.filter(h => selectedHuespedes.has(h.numeroDocumento));
    
    // Envía los huéspedes seleccionados al componente padre
    onSelectionSubmit(finalSelection);
    };
    
    const handleCancelar = () => {
        setShowCancelModal(true);
    };

    const handleSiguiente = () => {
        if (busquedaRealizada && searchResults.length === 0) {
            onSelectionSubmit([]); 
            return;
        }
        handleSubmitSelection();
    };

return (
<div className="buscar-huesped-layout">
  {/* PANEL IZQUIERDO */}
    <div className="left-pane">
        <div className="header-title-box">
        <h1>Buscar<br />Huésped</h1>
        </div>

        <form onSubmit={handleSearch} className="form-container">
        <InputField label="Nombre" name="nombre" value={huespedData.nombre} onChange={handleChange} error={errors.nombre} />
        <InputField label="Apellido" name="apellido" value={huespedData.apellido} onChange={handleChange} error={errors.apellido} />
        <DocumentoField tipoDocumento={huespedData.tipoDocumento} numeroDocumento={huespedData.numeroDocumento} onChange={handleChange} error={errors.numeroDocumento} highlight={!!errors.numeroDocumento} />

        <div className="form-actions">
            <button type="button" className="btn-cancel" onClick={handleCancelar}>Cancelar</button>
            <button type="submit" className="btn-search" disabled={isLoading}>
            {isLoading ? '...' : 'Aceptar'}
            </button>
        </div>
        </form>
    </div>

  {/* PANEL DERECHO */}
    <div className="right-pane">
        {!busquedaRealizada ? (
        <div className="empty-state"></div>
        ) : (
        <div className="results-card">
            <div className="results-header">Resultados de la búsqueda</div>
            <div className="table-scroll-container">
            <table className="custom-table">
                <thead>
                <tr>
                    <th onClick={() => handleSort('nombre')} style={{ cursor: 'pointer' }}>
                    Nombre {getSortIcon('nombre')}
                    </th>
                    <th onClick={() => handleSort('apellido')} style={{ cursor: 'pointer' }}>
                    Apellido {getSortIcon('apellido')}
                    </th>
                    <th onClick={() => handleSort('tipoDocumento')} style={{ cursor: 'pointer' }}>
                    Tipo {getSortIcon('tipoDocumento')}
                    </th>
                    <th onClick={() => handleSort('numeroDocumento')} style={{ cursor: 'pointer' }}>
                    Nro Documento {getSortIcon('numeroDocumento')}
                    </th>
                </tr>
                </thead>
                <tbody>
                {resultadosOrdenados.length > 0 ? (
                    resultadosOrdenados.map((h) => (
                    <tr 
                        key={h.numeroDocumento}
                        tabIndex={0} 
                        onClick={() => toggleHuespedSelection(h.numeroDocumento)}
                        onKeyDown={(e) => {
                        if (e.key === 'Enter' || e.key === ' ') {
                            e.preventDefault(); 
                            toggleHuespedSelection(h.numeroDocumento);
                        }
                        }}
                        className={selectedHuespedes.has(h.numeroDocumento) ? 'selected-row' : ''}
                        style={{ cursor: 'pointer', outline: 'none' }} 
                    >
                        <td>{h.nombre}</td>
                        <td>{h.apellido}</td>
                        <td>{h.tipoDocumento}</td>
                        <td>{h.numeroDocumento}</td>
                    </tr>
                    ))
                ) : (
                    <tr>
                    <td colSpan={4} style={{textAlign: 'center', padding: '20px'}}>
                        No se encontraron resultados. Presione "Siguiente" para dar de alta.
                    </td>
                    </tr>
                )}
                </tbody>
            </table>
            </div>
            <div className="results-footer">
            <button 
                type="button" 
                className="btn-next" 
                onClick={handleSiguiente}
                style={{ position: 'relative', zIndex: 10000, cursor: 'pointer' }}
            >
                Aceptar
            </button>
            </div>
        </div>
        )}
        <ModalError
            show={showErrorModal}
            message={errorMessage}
            onClose={() => setShowErrorModal(false)}
        />
        <ModalConfirmacion
                show={showCancelModal}
                title="CANCELAR"
                message="¿Desea cancelar esta Ocupación? El caso de uso finalizará." 
                icon="⚠️"
                closeText="NO"
                confirmText="SI"
                onClose={() => setShowCancelModal(false)}
                onConfirm={handleConfirmCancel}
        />
        
    </div>
</div>
)
};

export default HuespedSearchAndSelect;