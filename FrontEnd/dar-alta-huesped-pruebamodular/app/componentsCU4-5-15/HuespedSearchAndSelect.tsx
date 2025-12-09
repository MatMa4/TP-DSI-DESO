'use client';
import React, { useState, useMemo } from 'react';
import { InputField } from './InputField';
import DocumentoField from './DocumentoField';
import ModalError from './ModalError';
import { validateBuscarForm } from '../buscarHuesped/ValidacionDatosCU2'; 
import { HuespedDTOCompleto } from '../types/indexCU4-5-15'; 
import '../styles/stylesCU2.css'; 


// Props que recibe del componente padre (ocupar/page.tsx)
interface HuespedSearchAndSelectProps {
    onSelectionSubmit: (selectedHuespedes: HuespedDTOCompleto[]) => void;
    onCancel: () => void;
}


const HuespedSearchAndSelect: React.FC<HuespedSearchAndSelectProps> = ({ onSelectionSubmit, onCancel }) => {
    
    const [huespedData, setHuespedData] = useState<HuespedDTOCompleto>({
    nombre: '',
    apellido: '',
    tipoDocumento: 'DNI', 
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
    const [errors, setErrors] = useState<Record<string, string>>({});
    const [isLoading, setIsLoading] = useState(false);
    const [busquedaRealizada, setBusquedaRealizada] = useState(false);
    
    // --- ESTADOS ADAPTADOS PARA SELECCIÓN MÚLTIPLE (Panel Derecho) ---
    const [searchResults, setSearchResults] = useState<HuespedDTOCompleto[]>([]); // Resultados de la búsqueda [cite: 10]
    const [selectedHuespedes, setSelectedHuespedes] = useState<Set<string>>(new Set()); // Guarda NroDocumento de los seleccionados
    const [sortConfig, setSortConfig] = useState<SortConfig | null>(null); 
    
    // --- ESTADOS DE MODAL ---
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    // --- FUNCIONES COPIADAS DE BuscarHuesped ---
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        // Forzar mayúsculas en Nombre y Apellido 
        const valorFinal = (name === 'nombre' || name === 'apellido') ?
            value.toUpperCase() : value;

        setHuespedData(prev => ({ ...prev, [name]: valorFinal }));
        
        // Limpiar error al escribir [cite: 15, 16]
        if (errors[name]) {
            setErrors(prev => {
                const newErrors = { ...prev };
                delete newErrors[name];
                return newErrors;
            });
        }
    };

    const validateBuscarForm = (data: HuespedDTOCompleto): Record<string, string> => {
        const validationErrors: Record<string, string> = {};
        if (!data.apellido.trim() && !data.nombre.trim() && !data.numeroDocumento.trim()) {
            validationErrors.general = 'Debe ingresar al menos un criterio de búsqueda.';
        }
        return validationErrors;
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
            // 2. Construir URL dinámica (Copiada del código del compañero) [cite: 20, 21, 22]
            const params = new URLSearchParams();
            if (huespedData.nombre.trim()) params.append('nombre', huespedData.nombre.trim());
            if (huespedData.apellido.trim()) params.append('apellido', huespedData.apellido.trim());
            
            if (huespedData.numeroDocumento.trim()) {
                params.append('numeroDocumento', huespedData.numeroDocumento.trim());
                params.append('tipoDocumento', huespedData.tipoDocumento);
            }

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
    const handleSort = (key: keyof HuespedDTOCompleto) => {
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
                const key = sortConfig.key as keyof HuespedDTOCompleto;
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
    
    const getSortIcon = (key: keyof HuespedDTOCompleto) => {
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

        const finalSelection = searchResults.filter(h => selectedHuespedes.has(h.numeroDocumento));
        onSelectionSubmit(finalSelection);
    };
    
    return (
        <div className="huesped-search-container" style={{ display: 'flex', gap: '30px', padding: '20px' }}>
            
            {/* --- PANEL IZQUIERDO --- */}
            <div className="search-form-panel" style={{ flex: '0 0 350px', padding: '20px', backgroundColor: '#D9D9D9', borderRadius: '8px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)' }}>
                <div className="left-pane">
                    <div className="header-title-box">
                        <h1>Buscar<br />Huésped</h1>
                    </div>

                    <form onSubmit={handleSearch} className="form-container">
                        <InputField label="Nombre" name="nombre" value={huespedData.nombre} onChange={handleChange} error={errors.nombre} />
                        <InputField label="Apellido" name="apellido" value={huespedData.apellido} onChange={handleChange} error={errors.apellido} />
                        
                        {/* DocumentoFieldCU2 debe ser importado y usado correctamente */}
                        <DocumentoField 
                            tipoDocumento={huespedData.tipoDocumento} 
                            numeroDocumento={huespedData.numeroDocumento} 
                            onChange={handleChange} 
                            error={errors.numeroDocumento} 
                            highlight={!!errors.numeroDocumento} 
                        />
                        
                        <div className="form-actions">
                            <button type="button" className="btn-cancel" onClick={onCancel}>Cancelar</button>
                            <button type="submit" className="btn-accept" disabled={isLoading}>
                                {isLoading ? '...' : 'Buscar'} 
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            {/* --- PANEL DERECHO: Grilla de Selección (ADAPTADA) --- */}
            <div className="right-pane" style={{ flex: '1', padding: '20px', backgroundColor: '#fff', border: '1px solid #ccc', borderRadius: '8px' }}>
                <h3 style={{ borderBottom: '2px solid #ccc', paddingBottom: '10px', marginBottom: '15px', color: '#022E66' }}>
                    Resultados de la Búsqueda ({searchResults.length})
                </h3>

                <div className="huesped-results-table-container" style={{ maxHeight: '450px', overflowY: 'auto' }}>
                    {isLoading && busquedaRealizada ? (
                        <p style={{ textAlign: 'center', padding: '50px' }}>Buscando...</p>
                    ) : searchResults.length > 0 ? (
                        <table className="custom-table" style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                                <tr>
                                    <th style={{ width: '40px' }}>Sel.</th>
                                    <th onClick={() => handleSort('nombre')} style={{ cursor: 'pointer' }}>Nombre {getSortIcon('nombre')} </th>
                                    <th onClick={() => handleSort('apellido')} style={{ cursor: 'pointer' }}>Apellido {getSortIcon('apellido')} </th>
                                    <th onClick={() => handleSort('tipoDocumento')} style={{ cursor: 'pointer' }}>Tipo {getSortIcon('tipoDocumento')} </th>
                                    <th onClick={() => handleSort('numeroDocumento')} style={{ cursor: 'pointer' }}>Nro Documento {getSortIcon('numeroDocumento')} </th>
                                </tr>
                            </thead>
                            <tbody>
                                {/* Usamos resultadosOrdenados */}
                                {resultadosOrdenados.map((h) => (
                                    <tr 
                                        key={h.numeroDocumento}
                                        onClick={() => toggleHuespedSelection(h.numeroDocumento)}
                                        className={selectedHuespedes.has(h.numeroDocumento) ? 'selected-row' : ''} 
                                    >
                                        <td>
                                            <input 
                                                type="checkbox" 
                                                checked={selectedHuespedes.has(h.numeroDocumento)}
                                                readOnly 
                                            />
                                        </td>
                                        <td>{h.nombre}</td> 
                                        <td>{h.apellido}</td>
                                        <td>{h.tipoDocumento}</td>
                                        <td>{h.numeroDocumento}</td> 
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ) : busquedaRealizada && !isLoading ? (
                        <p style={{ textAlign: 'center', padding: '50px', color: '#666' }}>
                            No se encontraron resultados. 
                        </p>
                    ) : (
                        <p style={{ textAlign: 'center', padding: '50px', color: '#666' }}>
                            Utilice el panel de la izquierda para buscar huéspedes.
                        </p>
                    )}
                </div>
                
                {/* Botones de Acción */}
                <div className="results-footer">
                    <button className="btn-cancel" type="button" onClick={onCancel}>Cancelar</button>
                    <button className="btn-accept" type="button" onClick={handleSubmitSelection} disabled={selectedHuespedes.size === 0}>
                        Aceptar Selección
                    </button>
                </div>
            </div>

            {/* Modal de Error */}
            <ModalError
                show={showErrorModal}
                message={errorMessage}
                onClose={() => setShowErrorModal(false)}
            />
        </div>
    );
};

export default HuespedSearchAndSelect;