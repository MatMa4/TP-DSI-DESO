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
    tipoDocumento: 'DNI', 
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

    try {
        const params = new URLSearchParams();
        if (formData.nombre.trim()) params.append('nombre', formData.nombre.trim());
        if (formData.apellido.trim()) params.append('apellido', formData.apellido.trim());
        if (formData.numeroDocumento.trim()) {
            params.append('numeroDocumento', formData.numeroDocumento.trim());
            params.append('tipoDocumento', formData.tipoDocumento);
        }

        const queryString = params.toString();
        // Ajusta el puerto 8080 si tu backend corre en otro
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
        alert("Error de conexión con el Backend.");
    } finally {
        setIsLoading(false);
    }
  };

  // --- NAVEGACIÓN "FUERZA BRUTA" (Funciona siempre) ---
  
  const handleSiguiente = () => {
      // CASO 1: Huésped seleccionado -> Simulamos CU10
      if (seleccionadoId) {
          alert(`Simulación: Ir a Modificar Huésped (DNI: ${seleccionadoId})`);
      } 
      // CASO 2: Sin selección o sin resultados -> Ir a ALTA (CU09)
      else {
          console.log("Redirigiendo a Alta de Huésped...");
          // Usamos window.location para forzar la carga, igual que un link normal
          window.location.href = '/darAltaHuesped';
      }
  };

  const handleCancelar = () => {
      // Volver al inicio (Menú principal)
      window.location.href = '/';
  };

  return (
    <div className="buscar-huesped-layout">
      {/* IZQUIERDA: FORMULARIO */}
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
                <button type="button" className="btn-cancel" onClick={handleCancelar}>Cancelar</button>
                <button type="submit" className="btn-search" disabled={isLoading}>
                    {isLoading ? '...' : 'Buscar'}
                </button>
            </div>
        </form>
      </div>

      {/* DERECHA: RESULTADOS */}
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
                    {/* Botón Siguiente con ESTILOS FORZADOS para asegurar el clic */}
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