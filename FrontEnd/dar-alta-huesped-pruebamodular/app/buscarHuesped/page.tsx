'use client';

import React, { useState, useMemo } from 'react';
import InputField from '../components/InputField';
import DocumentoFieldCU2 from '../components/DocumentoFieldCU2';
import { validateBuscarForm } from './ValidacionDatosCU2'; 
import '../styles/stylesCU2.css';

// Interfaz del formulario de búsqueda (filtros)
interface FormData {
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  numeroDocumento: string;
}

// --- ACTUALIZADO: Interfaz completa del DTO que llega del Backend ---
interface Direccion {
  calle: string;
  numero: string;
  departamento: string;
  piso: string;
  codigo: string;
  localidad: string;
  provincia: string;
  pais: string;
}

interface HuespedResultado {
  // Datos principales
  id?: string; // Si tu backend manda ID, inclúyelo
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  numeroDocumento: string;
  fechaNacimiento: string;
  telefono: string;
  email: string;
  ocupacion: string;
  nacionalidad: string;
  cuit: string;
  posicionIVA: string;
  alojado: boolean;
  // Objeto anidado de dirección
  direccionHuesped: Direccion; 
}

interface SortConfig {
  key: keyof HuespedResultado; 
  direction: 'asc' | 'desc';
}

const BuscarHuesped = () => {
  const [formData, setFormData] = useState<FormData>({
    nombre: '', apellido: '', tipoDocumento: '', numeroDocumento: '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  // Ahora 'resultados' almacenará los objetos COMPLETOS
  const [resultados, setResultados] = useState<HuespedResultado[]>([]);
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);
  const [seleccionadoId, setSeleccionadoId] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [sortConfig, setSortConfig] = useState<SortConfig | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    const valorFinal = (name === 'nombre' || name === 'apellido') ? value.toUpperCase() : value;
    setFormData(prev => ({ ...prev, [name]: valorFinal }));
    if (errors[name]) {
        setErrors(prev => { const newErrors = { ...prev }; delete newErrors[name]; return newErrors; });
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

        const urlFinal = `http://localhost:8080/huespedes/buscar?${params.toString()}`;
        
        console.log('Buscando en:', urlFinal);

        const response = await fetch(urlFinal, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        if (!response.ok) throw new Error(`Error: ${response.status}`);

        const data = await response.json();
        // Asumimos que 'data' ya es la lista de DTOs completos
        setResultados(data);
        setBusquedaRealizada(true);

    } catch (error) {
        console.error("Error:", error);
        alert("Error de conexión con el Backend.");
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
        // @ts-ignore
        if (a[sortConfig.key] < b[sortConfig.key]) return sortConfig.direction === 'asc' ? -1 : 1;
        // @ts-ignore
        if (a[sortConfig.key] > b[sortConfig.key]) return sortConfig.direction === 'asc' ? 1 : -1;
        return 0;
      });
    }
    return sortedData;
  }, [resultados, sortConfig]);

  const getSortIcon = (key: keyof HuespedResultado) => {
    if (!sortConfig || sortConfig.key !== key) return null; 
    return sortConfig.direction === 'asc' ? ' ▲' : ' ▼';
  };

  // --- LÓGICA DE NAVEGACIÓN MODIFICADA ---
  const handleSiguiente = () => {
    // CASO 1: Hay un huésped seleccionado
    if (seleccionadoId) {
        // Buscamos el objeto COMPLETO en la memoria (ya lo tenemos del search)
        const huespedCompleto = resultados.find(h => h.numeroDocumento === seleccionadoId);

        if (!huespedCompleto) {
            alert("Error: No se encontraron los datos en memoria.");
            return;
        }
console.log("🔍 DATOS QUE LLEGARON DEL BACKEND:", huespedCompleto);
        console.log("Transfiriendo datos al CU10:", huespedCompleto);

        // 1. Guardamos el objeto completo en localStorage
        localStorage.setItem('datosHuespedModificar', JSON.stringify(huespedCompleto));

        // 2. Navegamos al CU10 (sin params en la URL para que quede limpio)
        window.location.href = '/modificarHuesped';

    } 
    // CASO 2: Sin selección -> Ir a ALTA (CU09)
    else {
        console.log("Redirigiendo a Alta de Huésped...");
        window.location.href = '/darAltaHuesped';
    }
  };

  const handleCancelar = () => {
      window.location.href = '/menuCU1';
  };

  return (
    <div className="buscar-huesped-layout">
      <div className="left-pane">
        <div className="header-title-box"><h1>Buscar<br />Huésped</h1></div>
        <form onSubmit={handleSearch} className="form-container">
            <InputField label="Nombre" name="nombre" value={formData.nombre} onChange={handleChange} error={errors.nombre} />
            <InputField label="Apellido" name="apellido" value={formData.apellido} onChange={handleChange} error={errors.apellido} />
            <DocumentoFieldCU2 tipoDocumento={formData.tipoDocumento} numeroDocumento={formData.numeroDocumento} onChange={handleChange} error={errors.numeroDocumento} highlight={!!errors.numeroDocumento} />
            <div className="form-actions">
                <button type="button" className="btn-cancel" onClick={handleCancelar}>Cancelar</button>
                <button type="submit" className="btn-search" disabled={isLoading}>{isLoading ? '...' : 'Buscar'}</button>
            </div>
        </form>
      </div>

      <div className="right-pane">
        {!busquedaRealizada ? (<div className="empty-state"></div>) : (
            <div className="results-card">
                <div className="results-header">Resultados de la búsqueda</div>
                <div className="table-scroll-container">
                    <table className="custom-table">
                        <thead>
                            <tr>
                                <th onClick={() => handleSort('nombre')}>Nombre {getSortIcon('nombre')}</th>
                                <th onClick={() => handleSort('apellido')}>Apellido {getSortIcon('apellido')}</th>
                                <th onClick={() => handleSort('tipoDocumento')}>Tipo {getSortIcon('tipoDocumento')}</th>
                                <th onClick={() => handleSort('numeroDocumento')}>Nro Documento {getSortIcon('numeroDocumento')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            {resultadosOrdenados.length > 0 ? (
                                resultadosOrdenados.map((h) => (
                                    <tr 
                                        key={h.numeroDocumento}
                                        tabIndex={0} 
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
                                <tr><td colSpan={4} style={{textAlign: 'center', padding: '20px'}}>No se encontraron resultados. Presione "Siguiente" para dar de alta.</td></tr>
                            )}
                        </tbody>
                    </table>
                </div>
                <div className="results-footer">
                    <button type="button" className="btn-next" onClick={handleSiguiente}>Siguiente</button>
                </div>
            </div>
        )}
      </div>
    </div>
  );
};

export default BuscarHuesped;