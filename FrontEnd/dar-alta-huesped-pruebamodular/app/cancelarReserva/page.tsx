'use client';
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';

import { InputField } from '../componentsCU4-5-15/InputField'; 
import ModalError from '../componentsCU4-5-15/ModalError';
import ModalConfirmarCancelacion from './ModalConfirmarCancelacion';
import ModalFin from './ModalFin';

import ListaReservas from './Listareservas';
import './stylesCancelarReserva.css'; 
import { ReservaDTO, CriterioBusquedaReserva } from './interfaces';


export default function CancelarReservaPage() {
    const router = useRouter();

    // --- ESTADOS ---
    const [criterios, setCriterios] = useState<CriterioBusquedaReserva>({
        nombre: '',
        apellido: '',
    });

    const [reservasResultados, setReservasResultados] = useState<ReservaDTO[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    
    //Confirmación
    const [showConfirmModal, setShowConfirmModal] = useState(false);
    const [selectedReservas, setSelectedReservas] = useState<ReservaDTO[]>([]);
    // Estados para errores
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    //Finalizado
    const [showExitoModal, setShowExitoModal] = useState(false);
    const [exitoMessage, setExitoMessage] = useState('');


    // --- HANDLERS ---
    const BASE_URL = 'http://localhost:8080';

    const handleChange = (e: React.ChangeEvent<any>) => {
        const { name, value } = e.target;
        setCriterios(prev => ({ ...prev, [name]: value }));
    };

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setReservasResultados([]);

        // Validación 
        if (!criterios.nombre && !criterios.apellido) {
            setErrorMessage("Por favor ingrese al menos un criterio de búsqueda.");
            setShowErrorModal(true);
            setIsLoading(false);
            return;
        }
        if (!criterios.apellido) {
            setErrorMessage("El campo apellido no puede estar vacio.");
            setShowErrorModal(true);
            setIsLoading(false);
            return;
        }


        try {
            const params = new URLSearchParams();
            
            if (criterios.apellido) {
            params.append('apellido', criterios.apellido.trim());
            }
            if (criterios.nombre !== undefined) { 
            params.append('nombre', criterios.nombre.trim());
            }
            if (params.toString() === '' && !criterios.apellido ) {
                throw new Error("Por favor ingrese al menos el apellido.");
            }

            const url = `${BASE_URL}/reservas/buscar?${params.toString()}`;
            console.log(url);
            const response = await fetch(url);

            if (!response.ok) {
                if (response.status === 404) {
                    setReservasResultados([]); 
                    setIsLoading(false);
                    return;
                }
                throw new Error("Error al buscar reservas.");
            }

            const data = await response.json();
            
            if (Array.isArray(data)) {
                setReservasResultados(data);
            } else {
                setReservasResultados([]);
            }

        } catch (error) {
            setErrorMessage("Error de conexión: " + (error instanceof Error ? error.message : 'Intente nuevamente.'));
            setShowErrorModal(true);
            setReservasResultados([]);
        } finally {
            setIsLoading(false);
        }
    };

    const handleIniciarCancelacion = () => {
        if (selectedReservas.length === 0) return;
        setShowConfirmModal(true);
    };

    const handleConfirmarCancelacion = async () => {
        setIsLoading(true);
        
        const url = `${BASE_URL}/reservas/cancelar`; 

        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(selectedReservas)
            });

            if (!response.ok) throw new Error("Error al cancelar reservas");
            
            const idsCancelados = selectedReservas.map(r => r.idReserva);

            setReservasResultados(prev => 
                prev.map(r => idsCancelados.includes(r.idReserva) ? { ...r, estado: 'CANCELADA' } : r)
            );

            setSelectedReservas([]);
            setShowConfirmModal(false);
            setExitoMessage(`${idsCancelados.length} reservas canceladas correctamente.\nPresione cualquer tecla para continuar...`);
            setShowExitoModal(true);

        } catch (error) {
            setErrorMessage("No se pudo cancelar la/s reserva/s: " + (error instanceof Error ? error.message : 'Error desconocido'));
            setShowErrorModal(true);
        } finally {
            setIsLoading(false);
        }
    };

    const handleToggleReserva = (reserva: ReservaDTO) => {
        setSelectedReservas(prev => {
            const exists = prev.some(r => r.idReserva === reserva.idReserva);
            if (exists) {
                return prev.filter(r => r.idReserva !== reserva.idReserva); 
            } else {
                return [...prev, reserva]; 
            }
        });
    };

    const handleCerrarExitoModal = () => {
    setShowExitoModal(false);
    router.push('/menuCU1');
    };

    return (
        <main className="main-container-cancelar">
            <div className="cancelar-layout">
                
                {/* PANEL IZQUIERDO */}
                <div className="left-pane-cancelar">
                    <div className="header-title-box">
                        <h1 className="main_title">Cancelar Reserva</h1>
                    </div>

                    <form onSubmit={handleSearch} className="form-container-cancelar">
                        <InputField 
                            label="Apellido" 
                            name="apellido" 
                            value={criterios.apellido} 
                            onChange={handleChange} 
                            type="text"
                        />
                        
                        <InputField 
                            label="Nombre" 
                            name="nombre" 
                            value={criterios.nombre} 
                            onChange={handleChange} 
                            type="text"
                        />
                        <div className="form-actions-cancelar">
                            <button type="button" className="btn-cancel" onClick={() => router.push('/menuCU1')}>
                                Volver
                            </button>
                            <button type="submit" className="btn-search" disabled={isLoading}>
                                {isLoading ? 'Buscando...' : 'Buscar Reserva'}
                            </button>
                        </div>
                    </form>
                </div>

                {/* PANEL DERECHO */}
                <div className="right-pane-cancelar">
                    <div className="results-box-cancelar">
                        <ListaReservas 
                            reservas={reservasResultados} 
                            selectedReservas={selectedReservas}
                            onToggleReserva={handleToggleReserva}
                        />
                    </div>
                </div>

                {/* BARRA FLOTANTE DE ACCIÓN */}
                    <div className={`bulk-actions-bar ${selectedReservas.length > 0 ? 'visible' : ''}`}>
                        <span className="bulk-counter">{selectedReservas.length} seleccionada(s)</span>
                        <button className="btn-cancelar-reserva" onClick={handleIniciarCancelacion}>
                            Cancelar Seleccionadas
                        </button>
                    </div>
            </div>
            
            <ModalConfirmarCancelacion
                show={showConfirmModal}
                reservas={selectedReservas} 
                onClose={() => setShowConfirmModal(false)}
                onConfirm={handleConfirmarCancelacion}
                isLoading={isLoading}
            />
            <ModalError
                show={showErrorModal}
                message={errorMessage}
                onClose={() => setShowErrorModal(false)}
            />
            <ModalFin
                show={showExitoModal}
                message={exitoMessage}
                onClose={handleCerrarExitoModal}
                onConfirm={handleCerrarExitoModal} 
            />
        </main>
    );
}