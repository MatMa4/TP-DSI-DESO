'use client';
import '../styles/stylesCU9prueba.css'; 
import React, { useState } from 'react';

// --- IMPORTACIONES DE COMPONENTES ---
// Asegúrate de que estos archivos existan en tu carpeta componentss
import InputField from '../components/InputField';
import DocumentoField from '../components/DocumentoField';
import DireccionHuesped from '../components/DireccionHuesped';
import ModalConfirmacion from '../components/ModalConfirmacion';
import ModalExito from '../components/ModalExito';

// --- IMPORTACIONES DE TIPOS Y LÓGICA ---
// Importamos los tipos desde el archivo centralizado
import { FormData } from '../types';
// Importamos la función de validación
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
  const [showCompletionScreen, setShowCompletionScreen] = useState(false);

  // --- MANEJADORES DE ESTADO ---

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    // @ts-ignore
    const checked = e.target.checked; 

    if (name.startsWith('direccionHuesped.')) {
      const field = name.split('.')[1];
      setFormData((prev) => ({
        ...prev,
        direccionHuesped: {
          ...prev.direccionHuesped,
          [field]: value,
        },
      }));
      setErrors((prev) => ({ ...prev, [name]: '' })); 
    } else {
      setFormData({
        ...formData,
        [name]: type === 'checkbox' ? checked : value,
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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // --- AQUÍ ESTÁ LA MAGIA MODULAR ---
    // En lugar de las 50 líneas de if/else, llamamos a la función importada:
    const newErrors = validateHuespedForm(formData);
    // ----------------------------------

    setErrors(newErrors);

    // Si no hay errores (objeto vacío), procedemos
    if (Object.keys(newErrors).length === 0) {
      
      // Transformación de datos (Mayúsculas, valores por defecto, etc.)
      const transformedData = {
        ...formData,
        numeroDocumento: formData.numeroDocumento ? formData.numeroDocumento.toUpperCase() : formData.numeroDocumento,
        tipoDocumento: formData.tipoDocumento ? formData.tipoDocumento.toUpperCase() : formData.tipoDocumento,
        apellido: formData.apellido ? formData.apellido.toUpperCase() : formData.apellido,
        nombre: formData.nombre ? formData.nombre.toUpperCase() : formData.nombre,
        telefono: formData.telefono? formData.telefono.toUpperCase() : formData.telefono,
        email: formData.email? formData.email.toUpperCase() : formData.email,
        ocupacion: formData.ocupacion? formData.ocupacion.toUpperCase() : formData.ocupacion,
        nacionalidad: formData.nacionalidad? formData.nacionalidad.toUpperCase() : formData.nacionalidad,
        cuit: formData.cuit,
        posicionIVA: formData.posicionIVA.trim()? formData.posicionIVA.toUpperCase() : "CONSUMIDOR FINAL",
        fechaNacimiento: formData.fechaNacimiento,
        alojado: formData.alojado,
        direccionHuesped: {
          calle: formData.direccionHuesped.calle? formData.direccionHuesped.calle.toUpperCase() : formData.direccionHuesped.calle,
          numero: formData.direccionHuesped.numero,
          piso: formData.direccionHuesped.piso,
          codigo: formData.direccionHuesped.codigo,
          departamento: formData.direccionHuesped.departamento? formData.direccionHuesped.departamento.toUpperCase() : formData.direccionHuesped.departamento,
          localidad: formData.direccionHuesped.localidad? formData.direccionHuesped.localidad.toUpperCase() : formData.direccionHuesped.localidad,
          provincia: formData.direccionHuesped.provincia? formData.direccionHuesped.provincia.toUpperCase() : formData.direccionHuesped.provincia,
          pais: formData.direccionHuesped.pais? formData.direccionHuesped.pais.toUpperCase() : formData.direccionHuesped.pais
        }
      };
      
      setFormData(transformedData);
      let finalData = transformedData;

      try {
        const res = await fetch('http://localhost:8080/huespedes', {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(finalData),
        });

        if (res.ok) {
          const saved = await res.json().catch(() => null);
          console.log('✅ Huésped guardado:', saved ?? 'No body');
          setSuccessMessage(
            `El huésped ${finalData.nombre ?? ''} ${finalData.apellido ?? ''} ha sido satisfactoriamente cargado al sistema\n¿Desea cargar otro?`
          );
          setShowSuccessModal(true);
          setPendingFinalData(null);
        } else if (res.status === 409) {
          setModalMessage('El tipo y número de documento ya existen en el sistema');
          setPendingFinalData(finalData); 
          setShowModal(true);
        }
      } catch (err) {
        console.error('❌ Error de conexión al backend:', err);
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
    try {
      const res = await fetch('http://localhost:8080/huespedes?forzar=true', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(pendingFinalData),
      });
      if (res.ok) {
        setSuccessMessage(
          `El huésped ${pendingFinalData.nombre ?? ''} ${pendingFinalData.apellido ?? ''} ha sido satisfactoriamente cargado al sistema\n¿Desea cargar otro?`
        );
        setShowSuccessModal(true);
      } else {
        console.error('Error al forzar registro:', res.status);
      }
    } catch (e) {
      console.error('Error de conexión al forzar:', e);
    } finally {
      setPendingFinalData(null);
    }
  };

  const handleCloseConflict = () => {
    setHighlightDocumento(true);
    setShowModal(false);
  };

  const handleConfirmCancel = () => {
    resetForm();
    setShowCancelModal(false);
    setShowCompletionScreen(true);
  };

  const handleSuccessClose = () => {
    setShowCompletionScreen(true);
    setShowSuccessModal(false);
  };

  const handleSuccessConfirm = () => {
    resetForm();
    setShowSuccessModal(false);
  };

  // --- RENDERIZADO (UI) ---
  return (
    <main>
      <div className="tittle_box">
        <h1 className="main_title">Completar los datos</h1>
      </div>

      <form className="main_box" onSubmit={handleSubmit} noValidate>
        <div className="container">
          <div className="box1">
            <InputField
              label="Nombre"
              name="nombre"
              value={formData.nombre}
              onChange={handleChange}
              error={errors.nombre}
              isRequired={true}
            />
            <InputField
              label="CUIT"
              name="cuit"
              value={formData.cuit}
              onChange={handleChange}
              error={errors.cuit}
            />
            <InputField
              label="Fecha de Nacimiento"
              name="fechaNacimiento"
              value={formData.fechaNacimiento}
              onChange={handleChange}
              error={errors.fechaNacimiento}
              type="date"
              isRequired={true}
            />
          </div>

          <div className="box1">
            <InputField
              label="Apellido"
              name="apellido"
              value={formData.apellido}
              onChange={handleChange}
              error={errors.apellido}
              isRequired={true}
            />
            <InputField
              label="Teléfono"
              name="telefono"
              value={formData.telefono}
              onChange={handleChange}
              error={errors.telefono}
              type="tel"
              isRequired={true}
            />
            <InputField
              label="Posición IVA"
              name="posicionIVA"
              value={formData.posicionIVA}
              onChange={handleChange}
              error={errors.posicionIVA}
            />
          </div>

          <DocumentoField
            tipoDocumento={formData.tipoDocumento}
            numeroDocumento={formData.numeroDocumento}
            onChange={handleChange}
            error={errors.numeroDocumento}
            highlight={highlightDocumento}
          />
        </div>

        <DireccionHuesped
          direccion={formData.direccionHuesped}
          onChange={handleChange}
          errors={errors}
        />

        <div className="container">
          <div className="box1">
            <InputField
              label="Email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              error={errors.email}
              type="email"
            />
          </div>
          <div className="box1">
            <InputField
              label="Ocupación"
              name="ocupacion"
              value={formData.ocupacion}
              onChange={handleChange}
              error={errors.ocupacion}
              isRequired={true}
            />
          </div>
          <div className="box1">
            <InputField
              label="Nacionalidad"
              name="nacionalidad"
              value={formData.nacionalidad}
              onChange={handleChange}
              error={errors.nacionalidad}
              isRequired={true}
            />
          </div>
        </div>

        <div className="container">
          <div className="box1">
            <button className="button2" type="button" onClick={handleCancelClick}>
              CANCELAR
            </button>
          </div>
          <div className="box1">
            <p>
              <small><span style={{ color: 'red' }}>*</span> Campos obligatorios</small>
            </p>
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

      {showCompletionScreen && (
        <div className="completion-screen">
          Caso de Uso Terminado
        </div>
      )}
    </main>
  );
};