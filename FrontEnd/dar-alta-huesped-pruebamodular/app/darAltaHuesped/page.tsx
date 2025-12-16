'use client';
import '../styles/stylesCU9prueba.css'; 
import React, { useState } from 'react';

// --- IMPORTACIONES DE COMPONENTES ---
import InputField from '../components/InputField';
import DocumentoField from '../components/DocumentoField';
import DireccionHuesped from '../components/DireccionHuesped';
import ModalConfirmacion from '../components/ModalConfirmacion';
import ModalExito from '../components/ModalExito';

// --- IMPORTACIONES DE TIPOS Y LÓGICA ---
import { FormData } from '../types';
import { validateHuespedForm } from './ValidacionLogicaErrores'; 

// Estado inicial 
const INITIAL_FORM: FormData = {
  numeroDocumento: '',
  tipoDocumento: 'DNI',
  apellido: '',
  nombre: '',
  fechaNacimiento: '',
  telefono: '',
  email: '',
  ocupacion: '',
  nacionalidad: '',
  cuit: '',
  posicionIVA: '',
  alojado: false,
  direccionHuesped: {
    calle: '',
    numero: '',
    departamento: '',
    piso: '',
    codigo: '',
    localidad: '',
    provincia: '',
    pais: '',
  },
};

export default function Home() {
  const [formData, setFormData] = useState<FormData>(INITIAL_FORM);
  const [errors, setErrors] = useState<Record<string, string>>({});
  
  // Estados para modales y feedback
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [pendingFinalData, setPendingFinalData] = useState<any | null>(null);
  
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [highlightDocumento, setHighlightDocumento] = useState(false);

  // --- MANEJADORES DE ESTADO ---

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    // @ts-ignore
    const checked = e.target.checked; 
    
    // LÓGICA DE MAYÚSCULAS:
    const valorFinal = type === 'checkbox' 
        ? checked 
        : (type === 'date' ? value : value.toUpperCase());

    if (name.startsWith('direccionHuesped.')) {
      const field = name.split('.')[1];
      setFormData((prev) => ({
         ...prev,
         direccionHuesped: {
           ...prev.direccionHuesped,
           [field]: valorFinal, 
         },
      }));
      setErrors((prev) => ({ ...prev, [name]: '' })); 
    } else {
      setFormData({
        ...formData,
        [name]: valorFinal, 
      });
      setErrors((prev) => ({ ...prev, [name]: '' }));

      if (name === 'numeroDocumento' || name === 'tipoDocumento') {
        setHighlightDocumento(false);
      }
    }
  };

  const handleCancelClick = (e: React.MouseEvent) => {
    e.preventDefault();
    setShowCancelModal(true);
  };

  // --- FUNCIÓN AUXILIAR PARA GUARDAR ---
  const guardarHuespedDirecto = async (dataAGuardar: any) => {
      try {
          const res = await fetch('http://localhost:8080/huespedes', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataAGuardar),
          });

          if (res.ok) {
            const saved = await res.json().catch(() => null);
            console.log('✅ Guardado exitoso:', saved);
            setSuccessMessage(
              `El huésped ${dataAGuardar.nombre} ${dataAGuardar.apellido} ha sido cargado correctamente.\n¿Desea cargar otro?`
            );
            setShowSuccessModal(true);
            setPendingFinalData(null);
          } else {
            console.error("Fallo al guardar:", res.status);
            alert("No se pudo guardar el huésped. Verifique los datos enviados.");
          }
      } catch (e) {
          console.error("Error en la petición de guardado:", e);
          alert("Error de red al intentar guardar.");
      }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const newErrors = validateHuespedForm(formData);
    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
  
      // 1. APLICAR TRIM() A TODO Y MAYÚSCULAS
      // Usamos una función auxiliar para evitar errores con nulls
      const safeTrim = (str: any) => (str || '').toString().trim();

      const transformedData = {
        ...formData, 
        // --- DATOS PERSONALES ---
        nombre: formData.nombre.replace(/\s+/g, ' ').trim().toUpperCase(),
        apellido: formData.apellido.replace(/\s+/g, ' ').trim().toUpperCase(),
        
        numeroDocumento: formData.numeroDocumento.trim(), 
        tipoDocumento: formData.tipoDocumento,
        telefono: formData.telefono.trim().toUpperCase(),
        
        email: formData.email.trim().toUpperCase(),
        ocupacion: formData.ocupacion.replace(/\s+/g, ' ').trim().toUpperCase(),
        nacionalidad: formData.nacionalidad.replace(/\s+/g, ' ').trim().toUpperCase(),
        
        cuit: formData.cuit.trim(),
        posicionIVA: formData.posicionIVA.trim() ? formData.posicionIVA.trim().toUpperCase() : "CONSUMIDOR FINAL",
        
        fechaNacimiento: formData.fechaNacimiento,
        alojado: formData.alojado,
        
        // --- DIRECCIÓN (Limpieza y Protección contra NULL) ---
        direccionHuesped: {
          calle: formData.direccionHuesped.calle.replace(/\s+/g, ' ').trim().toUpperCase(),
          departamento: formData.direccionHuesped.departamento.replace(/\s+/g, ' ').trim().toUpperCase(),
          localidad: formData.direccionHuesped.localidad.replace(/\s+/g, ' ').trim().toUpperCase(),
          provincia: formData.direccionHuesped.provincia.replace(/\s+/g, ' ').trim().toUpperCase(),
          pais: formData.direccionHuesped.pais.replace(/\s+/g, ' ').trim().toUpperCase(),
          numero: formData.direccionHuesped.numero.trim(),
          piso: formData.direccionHuesped.piso.trim(),
          codigo: (formData.direccionHuesped.codigo || '').trim(),
        }
      };
      
      setFormData(transformedData);
      let finalData = transformedData;
    
      // LOG PARA VER EL JSON QUE SE ENVÍA
      console.log("📦 DATOS LIMPIOS A ENVIAR:", JSON.stringify(finalData, null, 2));

      try {
        // Consultar Disponibilidad de DNI
        const params = new URLSearchParams();
        params.append('tipo', finalData.tipoDocumento);
        params.append('numero', finalData.numeroDocumento);

        const checkRes = await fetch(`http://localhost:8080/huespedes/consultarDocumento?${params.toString()}`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' }
        });

        // CASO 1: 200 OK -> NO EXISTE (Libre) -> GUARDAR
        if (checkRes.ok) {
            console.log("Documento disponible. Guardando...");
            await guardarHuespedDirecto(finalData);
        } 
        // CASO 2: 409 CONFLICT -> YA EXISTE -> MOSTRAR MODAL
        else if (checkRes.status === 409) {
            console.log("Conflicto: El huésped ya existe.");
            setPendingFinalData(finalData); 
            setModalMessage(`El huésped con ${finalData.tipoDocumento} ${finalData.numeroDocumento} ya existe en el sistema.\n¿Desea sobreescribir sus datos?`);
            setShowModal(true);
        } 
        else {
            console.error("Error inesperado:", checkRes.status);
            alert("Ocurrió un error al verificar el documento.");
        }

      } catch (err) {
        console.error('❌ Error de conexión:', err);
        alert("Error de conexión con el servidor.");
      }
    }
  };

  const resetForm = () => {
    setFormData(INITIAL_FORM);
    setErrors({});
  };

  // --- MANEJADORES DE CONFIRMACIÓN Y ÉXITO ---
  
  const handleConfirmConflict = async () => {
    setShowModal(false);
    setHighlightDocumento(false);
    if (!pendingFinalData) return;
    
    // Si confirma conflicto, usamos la misma lógica de guardado
    await guardarHuespedDirecto(pendingFinalData);
  };

  const handleCloseConflict = () => {
    setHighlightDocumento(true);
    setShowModal(false);
  };

  const handleConfirmCancel = () => {
    resetForm();
    setShowCancelModal(false);
    window.location.href = '/menuCU1';
  };

  const handleSuccessClose = () => {
    setShowSuccessModal(false);
    window.location.href = '/menuCU1';
  };

  const handleSuccessConfirm = () => {
    resetForm();
    setShowSuccessModal(false);
  };

  // --- UI ---
  return (
    <main>
      <div className="tittle_box">
        <h1 className="main_title">Completar los datos</h1>
      </div>

      <form className="main_box" onSubmit={handleSubmit} noValidate>
        <div className="container">
          <div className="box1">
            <InputField label="Nombre" name="nombre" value={formData.nombre} onChange={handleChange} error={errors.nombre} isRequired={true} />
            <InputField label="CUIT" name="cuit" value={formData.cuit} onChange={handleChange} error={errors.cuit} />
            <InputField label="Fecha de Nacimiento" name="fechaNacimiento" value={formData.fechaNacimiento} onChange={handleChange} error={errors.fechaNacimiento} type="date" isRequired={true} />
          </div>

          <div className="box1">
            <InputField label="Apellido" name="apellido" value={formData.apellido} onChange={handleChange} error={errors.apellido} isRequired={true} />
            <InputField label="Teléfono" name="telefono" value={formData.telefono} onChange={handleChange} error={errors.telefono} type="tel" isRequired={true} />
            <InputField label="Posición IVA" name="posicionIVA" value={formData.posicionIVA} onChange={handleChange} error={errors.posicionIVA} />
          </div>

          <DocumentoField tipoDocumento={formData.tipoDocumento} numeroDocumento={formData.numeroDocumento} onChange={handleChange} error={errors.numeroDocumento} highlight={highlightDocumento} />
        </div>

        <DireccionHuesped direccion={formData.direccionHuesped} onChange={handleChange} errors={errors} />

        <div className="container">
          <div className="box1">
            <InputField label="Email" name="email" value={formData.email} onChange={handleChange} error={errors.email} type="email" />
          </div>
          <div className="box1">
            <InputField label="Ocupación" name="ocupacion" value={formData.ocupacion} onChange={handleChange} error={errors.ocupacion} isRequired={true} />
          </div>
          <div className="box1">
            <InputField label="Nacionalidad" name="nacionalidad" value={formData.nacionalidad} onChange={handleChange} error={errors.nacionalidad} isRequired={true} />
          </div>
        </div>

        <div className="container">
          <div className="box1">
            <button className="button2" type="button" onClick={handleCancelClick}>
              CANCELAR
            </button>
          </div>
          <div className="box1">
            <p><small><span style={{ color: 'red' }}>*</span> Campos obligatorios</small></p>
          </div>
          <div className="box1">
            <button className="button2" type="submit">
              SIGUIENTE
            </button>
          </div>
        </div>
      </form>

      <ModalConfirmacion
        show={showModal}
        title="DNI Repetido"
        message={modalMessage}
        icon="⚠️"
        closeText="CORREGIR"
        confirmText="ACEPTAR IGUALMENTE"
        confirmClass="btn-accept yellow"
        onClose={handleCloseConflict}
        onConfirm={handleConfirmConflict}
      />

      <ModalConfirmacion
        show={showCancelModal}
        title="CANCELAR"
        message="¿Desea cancelar esta carga del huésped?"
        icon="⚠️"
        closeText="NO"
        confirmText="SI"
        confirmClass="btn-accept yellow"
        onClose={() => setShowCancelModal(false)}
        onConfirm={handleConfirmCancel}
      />

      <ModalExito
        show={showSuccessModal}
        message={successMessage}
        onClose={handleSuccessClose}
        onConfirm={handleSuccessConfirm}
      />
    </main>
  );
};