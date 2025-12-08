'use client'; 
import React, { useState, useEffect, useCallback } from 'react';
import DisponibilidadGrid from '../componentsCU4-5-15/DisponibilidadGrid-5';
import FiltrosHabitacion from '../componentsCU4-5-15/Filtroshabitacion-5';
import ModalError from '../componentsCU4-5-15/ModalError'; 
import { RoomCellData } from '../../types/indexCU4-5-15'; // Asegúrate de que la ruta sea correcta

// --- COMPONENTE PRINCIPAL ---
export default function MostrarEstadoHabitaciones() {
    // 1. Estados iniciales (vacíos por defecto)
    const [fechas, setFechas] = useState({ desde: '', hasta: '' });
    const [gridData, setGridData] = useState<RoomCellData[]>([]);
    const [selectedRoomType, setSelectedRoomType] = useState<string>('Doble'); 
    const [isLoading, setIsLoading] = useState(false);
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    
    // --- LÓGICA DE CONEXIÓN AL BACK-END ---
    const loadGridData = useCallback(async (desde: string, hasta: string, tipo: string) => {
        setIsLoading(true);
        setErrorMessage('');
        setGridData([]);
        
        const BASE_URL = 'http://localhost:8080';
        // URL con parámetros de búsqueda
        const url = `${BASE_URL}/api/habitaciones/disponibilidad?fechaInicio=${desde}&fechaFin=${hasta}&tipo=${tipo}`;
        
        try {
            const response = await fetch(url);
            
            if (!response.ok) {
                // Leer el mensaje de error del servidor si está disponible
                const errorBody = await response.json().catch(() => ({ message: response.statusText }));
                throw new Error(`Error ${response.status}: ${errorBody.message || 'Fallo al conectar.'}`);
            }
            
            // La data debe ser un array de RoomCellData[]
            const data: RoomCellData[] = await response.json(); 
            
            if (!Array.isArray(data) || data.length === 0) {
                setErrorMessage("No existen habitaciones disponibles con los criterios de búsqueda.");
                setShowErrorModal(true);
                return;
            }
            
            setGridData(data);
            setSelectedRoomType(tipo);

        } catch (error) {
            setErrorMessage(`Error de conexión o servidor: ${error.message}`);
            setShowErrorModal(true);
        } finally {
            setIsLoading(false);
        }
    }, []); // Dependencias vacías ya que solo usa estados y props internos

    // 2. Manejadores de Interfaz
    const handleFechasChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFechas(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSearch = (tipo: string) => {
        // Validación básica antes de la llamada a la API
        if (!fechas.desde || !fechas.hasta) {
            setErrorMessage('Debe seleccionar ambas fechas.');
            setShowErrorModal(true);
            return;
        }
        loadGridData(fechas.desde, fechas.hasta, tipo);
    };

    // La función no tiene useEffect para carga inicial; solo carga al hacer click en "Mostrar Estado"

    // --- RENDERIZADO (UI) ---
    return (
        <main className="main-container">
            <div className="tittle_box">
                <h1 className="main_title">Mostrar Estado Habitaciones</h1>
            </div>

            <div className="main_box" style={{ padding: '20px' }}>
                
                {/* 1. Filtros */}
                <FiltrosHabitacion 
                    fechas={fechas} 
                    onChange={handleFechasChange}
                    onSearch={handleSearch} 
                    onCancel={() => {}} 
                    error={''} // Manejamos el error en el modal global
                    searchButtonText="Mostrar Estado" 
                />

                {/* 2. Contenedor de la Grilla */}
                <div className='grid-container' style={{ 
                    border: gridData.length > 0 ? 'none' : '1px solid #ccc', 
                    backgroundColor: gridData.length > 0 ? 'transparent' : '#BEBCBC',
                    minHeight: '300px'
                }}>
                    {isLoading ? (
                        <p style={{ textAlign: 'center', padding: '100px' }}>Cargando disponibilidad...</p>
                    ) : gridData.length > 0 ? (
                        <DisponibilidadGrid 
                            fechas={fechas} 
                            gridData={gridData.filter(d => d.roomType === selectedRoomType)}
                            onGridSubmit={() => {}} // No hay acción de submit
                            onCancel={() => {}} 
                            isReadOnly={true} // Modo solo lectura
                        />
                    ) : (
                        <p style={{ textAlign: 'center', padding: '100px', color: '#666' }}>
                            Seleccione fechas y tipo, luego presione "Mostrar Estado"
                        </p>
                    )}
                </div>
            </div>
            
            {/* Modal de Error Global */}
            <ModalError
                show={showErrorModal}
                message={errorMessage}
                onClose={() => setShowErrorModal(false)}
            />
        </main>
    );
}