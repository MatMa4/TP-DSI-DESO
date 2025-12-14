'use client';
import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation'; // Importamos el router
import InputField from './components/InputField'; 
import './styles/stylesCU1.css'; 

export default function LoginPage() {
  const router = useRouter(); // Hook para navegar
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [generalError, setGeneralError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({ username: '', password: '' });
  const [loading, setLoading] = useState(false);

  // Opcional: Si ya hay sesión, redirigir directo al menú al entrar a /
  useEffect(() => {
    if (sessionStorage.getItem('userSessionActive') === 'true') {
        router.push('/menuCU1');
    }
  }, [router]);

  const validateForm = () => {
      let isValid = true;
      const newErrors = { username: '', password: '' };

      if (!username.trim()) { newErrors.username = 'El usuario es obligatorio.'; isValid = false; }
      if (!password.trim()) { newErrors.password = 'La contraseña es obligatoria.'; isValid = false; }

      setFieldErrors(newErrors);
      return isValid;
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setGeneralError('');
    if (!validateForm()) return;
    setLoading(true);

    try {
      const res = await fetch('http://localhost:8080/usuario/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, passw: password }),
      });

      if (res.ok) { 
        // 1. Guardamos sesión
        sessionStorage.setItem('userSessionActive', 'true');
        // 2. Redirigimos a la página del menú
        router.push('/menuCU1');
      } else if (res.status === 401) {
        setGeneralError('El usuario o la contraseña no son válidos');
      } else {
        setGeneralError('Ocurrió un error inesperado en el servidor.');
      }
    } catch (err) {
      console.error(err);
      setGeneralError('Error de conexión con el backend.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="mainContainer">
      <div className="loginCard">
        <h1 className="loginTitle">Iniciar Sesión</h1>
        <form onSubmit={handleLogin} className="loginForm" noValidate>
            <InputField 
                label="Usuario" name="username" value={username}
                onChange={(e) => setUsername(e.target.value)}
                error={fieldErrors.username} isRequired={true}
            />
            <InputField 
                label="Contraseña" name="password" type="password" value={password}
                onChange={(e) => setPassword(e.target.value)}
                error={fieldErrors.password} isRequired={true}
            />
            {generalError && <div className="errorMessage">⚠️ {generalError}</div>}
            <button type="submit" className="loginButton" disabled={loading}>
                {loading ? 'Verificando...' : 'INGRESAR'}
            </button>
        </form>
      </div>
    </main>
  );
}