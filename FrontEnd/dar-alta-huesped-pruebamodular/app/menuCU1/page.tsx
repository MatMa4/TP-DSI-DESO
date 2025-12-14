'use client';
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
// Importamos CSS subiendo un nivel
import '../styles/stylesCU1.css'; 

export default function MenuPage() {
  const router = useRouter();
  const [isAuthorized, setIsAuthorized] = useState(false);

  useEffect(() => {
    // PROTECCIÓN DE RUTA:
    // Si no hay marca en sessionStorage, lo mandamos al Login
    const sesionActiva = sessionStorage.getItem('userSessionActive');
    
    if (sesionActiva !== 'true') {
        router.push('/'); // Redirigir al login
    } else {
        setIsAuthorized(true); // Permitir mostrar el contenido
    }
  }, [router]);

  const handleLogout = () => {
      sessionStorage.removeItem('userSessionActive');
      router.push('/'); // Volver al login al salir
  };

  // Evitamos el parpadeo mostrando nada hasta verificar la autorización
  if (!isAuthorized) {
      return null; // O puedes retornar un <p>Cargando...</p> simple
  }

  return (
      <main className="mainContainer">
        <h1 className="mainTitle">🏨 Hotel Premier</h1>
        
        <button className="logoutButton" onClick={handleLogout}>Cerrar Sesión</button>

        <div className="dashboardGrid">
          <a href="/buscarHuesped" className="menuCard">
            <div className="menuIcon">🔍</div>
            <h3>Buscar Huésped</h3>
            <p>CU 02 - Buscar, modificar o dar de alta.</p>
          </a>
          <a href="/darAltaHuesped" className="menuCard">
            <div className="menuIcon">👤</div>
            <h3>Nuevo Huésped</h3>
            <p>CU 09 - Registro directo.</p>
          </a>
          <a href="/reservarHabitacion" className="menuCard">
            <div className="menuIcon">📅</div>
            <h3>Reservar</h3>
            <p>CU 04 - Gestión de reservas.</p>
          </a>
          <a href="/ocupacion" className="menuCard">
            <div className="menuIcon">🛏️</div>
            <h3>Ocupar Habitaciones</h3>
            <p>CU 15 - Check-in y ocupación.</p>
          </a>
        </div>
      </main>
  );
}