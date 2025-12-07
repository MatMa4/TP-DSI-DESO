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

// <--- NUEVO: Interfaz para la configuración del orden
interface SortConfig {
  key: keyof HuespedResultado; // Solo permite claves que existan en HuespedResultado
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

  // <--- NUEVO: Estado para el ordenamiento
  const [sortConfig, setSortConfig] = useState<SortConfig | null>(null);

  // --- MANEJADORES ---
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    // Forzar mayúsculas en Nombre y Apellido
    const valorFinal = (name === 'nombre' || name === 'apellido') ? value.toUpperCase() : value;

    setFormData(prev => ({ ...prev, [name]: valorFinal }));
    
    // Limpiar error al escribir
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
    
    // 1. Validar formato
    const validationErrors = validateBuscarForm(formData);
    if (Object.keys(validationErrors).length > 0) {
        setErrors(validationErrors);
        return;
    }

    setIsLoading(true);
    setSeleccionadoId(null);
    setResultados([]); 
    setSortConfig(null); // <--- NUEVO: Reseteamos el orden al hacer una nueva búsqueda

    try {
        // 2. Construir URL dinámica
        const params = new URLSearchParams();
        if (formData.nombre.trim()) params.append('nombre', formData.nombre.trim());
        if (formData.apellido.trim()) params.append('apellido', formData.apellido.trim());
        
        // Solo enviar documento si hay número escrito
        if (formData.numeroDocumento.trim()) {
            params.append('numeroDocumento', formData.numeroDocumento.trim());
            params.append('tipoDocumento', formData.tipoDocumento);
        }

        const queryString = params.toString();
        const urlFinal = `http://localhost:8080/huespedes/buscar${queryString ? `?${queryString}` : ''}`;
        
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

  // <--- NUEVO: Función para manejar el clic en los encabezados
  const handleSort = (key: keyof HuespedResultado) => {
    let direction: 'asc' | 'desc' = 'asc';
    
    // Si ya estamos ordenando por esta columna y es ascendente, cambiamos a descendente
    if (sortConfig && sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };

  // <--- NUEVO: Calculamos los resultados ordenados dinámicamente
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

  // <--- NUEVO: Helper para mostrar la flechita
  const getSortIcon = (key: keyof HuespedResultado) => {
    if (!sortConfig || sortConfig.key !== key) return null; // Sin icono
    return sortConfig.direction === 'asc' ? ' ▲' : ' ▼';
  };

  // --- NAVEGACIÓN "FUERZA BRUTA" (Infalible) ---
  const handleSiguiente = () => {
      // CASO 1: Selección -> Modificar (Simulado)
      if (seleccionadoId) {
          alert(`Simulación: Ir a Modificar Huésped (Documento: ${seleccionadoId})`);
      } 
      // CASO 2: Sin selección o sin resultados -> Ir a ALTA (CU09)
      else {
          console.log("Redirigiendo a Alta de Huésped...");
          window.location.href = '/darAltaHuesped';
      }
  };

  const handleCancelar = () => {
      // Volver al Menú Principal
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
                                {/* <--- NUEVO: Agregamos onClick y estilos a los TH */}
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
                            {/* <--- NUEVO: Usamos resultadosOrdenados en vez de resultados */}
                            {resultadosOrdenados.length > 0 ? (
                                resultadosOrdenados.map((h) => (
                                    <tr 
                                        key={h.numeroDocumento}
                                        onClick={() => setSeleccionadoId(h.numeroDocumento === seleccionadoId ? null : h.numeroDocumento)}
                                        className={seleccionadoId === h.numeroDocumento ? 'selected-row' : ''}
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
                    {/* Botón con Z-INDEX forzado para asegurar clic */}
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