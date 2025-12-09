'use client';

import React, { useState, useMemo } from 'react';
// Nota: Usamos window.location en lugar de useRouter para forzar la navegación en tu entorno
import InputField from '../components/InputField';
import DocumentoFieldCU2 from '../components/DocumentoFieldCU2';
import { validateBuscarForm } from './ValidacionDatosCU2'; 
import '../styles/stylesCU2.css'; // Asegurate de que este import coincida con tu CSS actual

interface FormData {
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  numeroDocumento: string;
}

interface HuespedResultado {
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  numeroDocumento: string;
}

// Interfaz para la configuración del orden
interface SortConfig {
  key: keyof HuespedResultado; 
  direction: 'asc' | 'desc';
}

const BuscarHuesped = () => {
  // --- ESTADOS ---
  const [formData, setFormData] = useState<FormData>({
    nombre: '',
    apellido: '',
    tipoDocumento: 'DNI', 
    numeroDocumento: '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [resultados, setResultados] = useState<HuespedResultado[]>([]);
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);
  const [seleccionadoId, setSeleccionadoId] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // Estado para el ordenamiento
  const [sortConfig, setSortConfig] = useState<SortConfig | null>(null);

  // --- MANEJADORES ---
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    const valorFinal = (name === 'nombre' || name === 'apellido') ? value.toUpperCase() : value;

    setFormData(prev => ({ ...prev, [name]: valorFinal }));
    
    if (errors[name]) {
        setErrors(prev => {
            const newErrors = { ...prev };
            delete newErrors[name];
            return newErrors;
        });
    }
  };

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    
    const validationErrors = validateBuscarForm(formData);
    if (Object.keys(validationErrors).length > 0) {
        setErrors(validationErrors);
        return;
    }

    setIsLoading(true);
    setSeleccionadoId(null);
    setResultados([]); 
    setSortConfig(null); 

    try {
        const params = new URLSearchParams();
        if (formData.nombre.trim()) params.append('nombre', formData.nombre.trim());
        if (formData.apellido.trim()) params.append('apellido', formData.apellido.trim());
        if (formData.numeroDocumento.trim()) params.append('numero', formData.numeroDocumento.trim());
        if (formData.tipoDocumento.trim()) params.append('tipo', formData.tipoDocumento);

        const queryString = params.toString();
        const urlFinal = `http://localhost:8080/huespedes/buscar?${queryString}`;
        
        console.log('Buscando en:', urlFinal);

        const response = await fetch(urlFinal, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        if (!response.ok) throw new Error(`Error: ${response.status}`);

        const data = await response.json();
        setResultados(data);
        setBusquedaRealizada(true);

    } catch (error) {
        console.error("Error:", error);
        alert("Error de conexión con el Backend. Revise que esté encendido.");
    } finally {
        setIsLoading(false);
    }
  };

  const handleSort = (key: keyof HuespedResultado) => {
    let direction: 'asc' | 'desc' = 'asc';
    
    if (sortConfig && sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };

  const resultadosOrdenados = useMemo(() => {
    let sortedData = [...resultados];
    if (sortConfig !== null) {
      sortedData.sort((a, b) => {
        if (a[sortConfig.key] < b[sortConfig.key]) {
          return sortConfig.direction === 'asc' ? -1 : 1;
        }
        if (a[sortConfig.key] > b[sortConfig.key]) {
          return sortConfig.direction === 'asc' ? 1 : -1;
        }
        return 0;
      });
    }
    return sortedData;
  }, [resultados, sortConfig]);

  const getSortIcon = (key: keyof HuespedResultado) => {
    if (!sortConfig || sortConfig.key !== key) return null; 
    return sortConfig.direction === 'asc' ? ' ▲' : ' ▼';
  };

  const handleSiguiente = async () => {
    // CASO 1: Hay un huésped seleccionado -> Validar con Back e ir a Modificar
    if (seleccionadoId) {
        
        const huespedElegido = resultados.find(h => h.numeroDocumento === seleccionadoId);

        if (!huespedElegido) {
            alert("Error: No se pudieron obtener los datos del huésped seleccionado.");
            return;
        }

        try {
            const params = new URLSearchParams();
            params.append('tipo', huespedElegido.tipoDocumento);
            params.append('numero', huespedElegido.numeroDocumento);

            const urlObtener = `http://localhost:8080/huespedes/obtener?${params.toString()}`;
            
            console.log("Consultando huésped en:", urlObtener);

            const response = await fetch(urlObtener, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            if (!response.ok) {
                throw new Error(`Error ${response.status}: No se pudo recuperar el huésped.`);
            }

            const huespedCompleto = await response.json();
            console.log("Huésped recibido:", huespedCompleto);

            alert(`¡Huésped encontrado en el Back!\n\nNombre: ${huespedCompleto.nombre} ${huespedCompleto.apellido}\nDocumento: ${huespedCompleto.tipoDocumento} ${huespedCompleto.numeroDocumento}\n\n(Redirigiendo al CU 10 Modificar Huesped...)`);
            
            // window.location.href = `/modificarHuesped?id=${huespedCompleto.id}`;

        } catch (error) {
            console.error(error);
            alert("Error de conexión: El backend no respondió correctamente al intentar obtener el huésped.");
        }

    } 
    // CASO 2: Sin selección -> Ir a ALTA (CU09)
    else {
        console.log("Redirigiendo a Alta de Huésped...");
        window.location.href = '/darAltaHuesped';
    }
  };

  const handleCancelar = () => {
      window.location.href = '/';
  };

  return (
    <div className="buscar-huesped-layout">
      {/* PANEL IZQUIERDO */}
      <div className="left-pane">
        <div className="header-title-box">
            <h1>Buscar<br />Huésped</h1>
        </div>

        <form onSubmit={handleSearch} className="form-container">
            <InputField label="Nombre" name="nombre" value={formData.nombre} onChange={handleChange} error={errors.nombre} />
            <InputField label="Apellido" name="apellido" value={formData.apellido} onChange={handleChange} error={errors.apellido} />
            <DocumentoFieldCU2 tipoDocumento={formData.tipoDocumento} numeroDocumento={formData.numeroDocumento} onChange={handleChange} error={errors.numeroDocumento} highlight={!!errors.numeroDocumento} />
            
            <div className="form-actions">
                <button type="button" className="btn-cancel" onClick={handleCancelar}>Cancelar</button>
                <button type="submit" className="btn-search" disabled={isLoading}>
                    {isLoading ? '...' : 'Buscar'}
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
                                        // 1. Permite seleccionar con TAB
                                        tabIndex={0} 
                                        
                                        // 2. Click normal con ratón
                                        onClick={() => setSeleccionadoId(h.numeroDocumento === seleccionadoId ? null : h.numeroDocumento)}
                                        
                                        // 3. Selección con teclado (Enter o Espacio)
                                        onKeyDown={(e) => {
                                            if (e.key === 'Enter' || e.key === ' ') {
                                                e.preventDefault(); // Evita scroll con espacio
                                                setSeleccionadoId(h.numeroDocumento === seleccionadoId ? null : h.numeroDocumento);
                                            }
                                        }}

                                        className={seleccionadoId === h.numeroDocumento ? 'selected-row' : ''}
                                        
                                        // Estilo para indicar visualmente el foco (outline)
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
                        Siguiente
                    </button>
                </div>
            </div>
        )}
      </div>
    </div>
  );
};

export default BuscarHuesped;