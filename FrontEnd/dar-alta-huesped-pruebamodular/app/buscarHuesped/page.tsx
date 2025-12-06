'use client';

import React, { useState } from 'react';
import InputField from '../components/InputField';
import DocumentoField from '../components/DocumentoField';
import { validateBuscarForm } from './ValidacionDatosCU2'; 
import '../styles/stylesCU2.css';

interface FormData {
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  numeroDocumento: string;
}

interface HuespedResultado {
  id: string; 
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  numeroDocumento: string;
}

const BuscarHuesped = () => {
  // --- ESTADOS ---
  const [formData, setFormData] = useState<FormData>({
    nombre: '',
    apellido: '',
    tipoDocumento: 'DNI', // Valor por defecto visual
    numeroDocumento: '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [resultados, setResultados] = useState<HuespedResultado[]>([]);
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);
  const [seleccionadoId, setSeleccionadoId] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // --- MANEJADORES ---

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    
    // Forzamos mayúsculas solo en Nombre y Apellido
    const valorFinal = (name === 'nombre' || name === 'apellido') 
      ? value.toUpperCase() 
      : value;

    setFormData(prev => ({ ...prev, [name]: valorFinal }));
    
    // Limpiamos errores visuales al escribir
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
    
    // 1. Validaciones de formato (solo si hay algo escrito)
    const validationErrors = validateBuscarForm(formData);
    if (Object.keys(validationErrors).length > 0) {
        setErrors(validationErrors);
        return;
    }

    setIsLoading(true);
    setSeleccionadoId(null);
    setResultados([]); 

    try {
        // --- 2. CONSTRUCCIÓN INTELIGENTE DE LA URL ---
        const params = new URLSearchParams();

        // A. Nombre: Solo si tiene texto
        if (formData.nombre.trim() !== '') {
            params.append('nombre', formData.nombre.trim());
        }

        // B. Apellido: Solo si tiene texto
        if (formData.apellido.trim() !== '') {
            params.append('apellido', formData.apellido.trim());
        }
        
        // C. Documento: LÓGICA CLAVE
        // Solo enviamos los datos del documento si el usuario escribió un NÚMERO.
        // Si enviamos siempre el 'tipoDocumento' (que por defecto es DNI), 
        // romperíamos la búsqueda por solo Nombre (porque filtraría solo DNIs).
        if (formData.numeroDocumento.trim() !== '') {
            params.append('numeroDocumento', formData.numeroDocumento.trim());
            params.append('tipoDocumento', formData.tipoDocumento);
        }

        const queryString = params.toString();
        
        // Si no hay parámetros (búsqueda vacía), el backend debería devolver todo o nada
        // según su configuración. Aquí asumimos que '/buscar' sin params funciona o falla controlado.
        const urlFinal = `http://localhost:8080/huespedes/buscar${queryString ? `?${queryString}` : ''}`;
        
        console.log('Fetching URL:', urlFinal); // Para depurar

        const response = await fetch(urlFinal, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        if (!response.ok) {
             throw new Error(`Error del servidor: ${response.status}`);
        }

        const data = await response.json();
        setResultados(data);
        setBusquedaRealizada(true);

    } catch (error) {
        console.error("Error al buscar:", error);
        alert("Error de conexión. Verifique que el Backend esté corriendo.");
    } finally {
        setIsLoading(false);
    }
  };

  const handleSiguiente = () => {
      if (seleccionadoId) {
          console.log(`Navegar a Modificar -> ID: ${seleccionadoId}`);
          // Aquí rediriges a la pantalla de Modificación
      } else {
          console.log("Navegar a Alta -> (Sin selección)");
          // Aquí rediriges a la pantalla de Alta
      }
  };

  return (
    <div className="buscar-huesped-layout">
      {/* PANEL IZQUIERDO */}
      <div className="left-pane">
        <div className="header-title-box">
            <h1>Buscar<br />Huésped</h1>
        </div>

        <form onSubmit={handleSearch} className="form-container">
            <InputField 
                label="Nombre"
                name="nombre"
                value={formData.nombre}
                onChange={handleChange}
                error={errors.nombre}
            />
            <InputField 
                label="Apellido"
                name="apellido"
                value={formData.apellido}
                onChange={handleChange}
                error={errors.apellido}
            />
            <DocumentoField 
                tipoDocumento={formData.tipoDocumento}
                numeroDocumento={formData.numeroDocumento}
                onChange={handleChange}
                error={errors.numeroDocumento}
                highlight={!!errors.numeroDocumento}
            />
            <div className="form-actions">
                {/* Botón reload para limpiar el formulario rápido */}
                <button type="button" className="btn-cancel" onClick={() => window.location.reload()}>Cancelar</button>
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
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Tipo</th>
                                <th>Nro Documento</th>
                            </tr>
                        </thead>
                        <tbody>
                            {resultados.length > 0 ? (
                                resultados.map((h) => (
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
                    <button className="btn-next" onClick={handleSiguiente}>
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