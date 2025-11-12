'use client';
import { useState } from 'react';
import '../styles.css';
export default function Home() {

  const [formData, setFormData] = useState({
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
    posicionIVA: 'Consumidor Final',
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
  });
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type, checked } = e.target;

    // Si el input pertenece a la dirección (nombre con "direccionHuesped.")
    if (name.startsWith('direccionHuesped.')) {
      const field = name.split('.')[1];
      setFormData((prev) => ({
        ...prev,
        direccionHuesped: {
          ...prev.direccionHuesped,
          [field]: value,
        },
      }));
    } else {
      setFormData({
        ...formData,
        [name]: type === 'checkbox' ? checked : value,
      });
    }
  };
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (formData.nombre.trim().length < 2) {
    alert("El nombre debe tener al menos 2 caracteres.");
    return;
  }
    console.log(formData); // Por ahora solo muestra los datos en consola
  };

  return (
    <main>
       <div className="tittle_box">
          <h1 className="main_title">Completar los datos</h1>
        </div>

      <form className="main_box" onSubmit={handleSubmit}>
        <div className="container">
          <div className="box1">
            <div className="box1_simplebox">
              <label htmlFor="Nombre">Nombre *</label>
              <input 
                className="input_box" 
                type="text" 
                name="nombre"
                value ={formData.nombre}
                onChange={handleChange}
                required
              />
            </div>

            <div className="box1_simplebox">
              <label htmlFor="CUIT">CUIT</label>
              <input 
                className="input_box" 
                type="text" 
                name="cuit"
                value={formData.cuit}
                onChange={handleChange}
              />
            </div>

            <div className="box1_simplebox">
              <label htmlFor="Fecha">Fecha de Nacimiento *</label>
              <input 
                className="input_box" 
                type="date" 
                name="fechaNacimiento"
                value={formData.fechaNacimiento}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="box1">
            <div className="box1_simplebox">
              <label htmlFor="Apellido">Apellido *</label>
              <input 
                className="input_box" 
                type="text" 
                name="apellido"
                value={formData.apellido}
                onChange={handleChange}
                required
              />
            </div>

            <div className="box1_simplebox">
              <label htmlFor="posicionIVA">posicionIVA *</label>
              <input 
                className="input_box" 
                type="text" 
                name="posicionIVA"
                value={formData.posicionIVA}
                onChange={handleChange}
                required
              />
            </div>

            <div className="box1_simplebox">
              <label>Teléfono *</label>
              <input 
                className="input_box" 
                type="tel" 
                name="telefono" 
                value={formData.telefono}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="box1 boxDocumento">
            <label>Documento *</label>
            <select 
            className="input_box" 
            name="tipoDocumento" 
            value={formData.tipoDocumento}
            onChange={handleChange}
            required>
              <option value="dni">DNI</option>
              <option value="LE">LE</option>
              <option value="LC">LC</option>
              <option value="pasaporte">Pasaporte</option>
              <option value="Otro">Otro</option>
            </select>
            <input 
              className="input_box_documento" 
              type="text" 
              name="numeroDocumento" 
              value={formData.numeroDocumento}
              onChange={handleChange}
              required
            />
          </div>
        </div>

         <div className='container'>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Calle *</label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.calle"
                      value={formData.direccionHuesped.calle}
                      onChange={handleChange}
                      required 
                    />
                </div>

                <div className="box1_simplebox">
                    <label>Codigo Postal</label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.codigo"
                      value={formData.direccionHuesped.codigo}
                      onChange={handleChange}
                      required 
                    />
                </div>

            </div>

            <div className="box1">
                    
                    <div className="box1_simplebox">
                        <label>Número *</label>
                        <input  
                          className="input_box" 
                          type="text" 
                          name="direccionHuesped.numero"
                          value={formData.direccionHuesped.numero}
                          onChange={handleChange}
                          required 
                        />
                    </div>
                    <div className="box1_simplebox">
                        <label>Localidad *</label>
                        <input  
                          className="input_box" 
                          type="text" 
                          name="direccionHuesped.localidad"
                          value={formData.direccionHuesped.localidad}
                          onChange={handleChange} 
                          required 
                        />    
                    </div>
            </div>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Piso *</label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.piso"
                      value={formData.direccionHuesped.piso}
                      onChange={handleChange}
                      required 
                    />
                </div>
                <div className="box1_simplebox">
                    <label>Provincia *</label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.provincia" 
                      value={formData.direccionHuesped.provincia}
                      onChange={handleChange}
                      required 
                    />
                </div>

                
            </div>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Departamento *</label>
                    <input 
                    className="input_box" 
                    type="text" 
                    name="direccionHuesped.departamento"
                    value={formData.direccionHuesped.departamento}
                    onChange={handleChange}
                    required 
                    />
                </div>
                <div className="box1_simplebox">
                    <label>País *</label>
                    <input 
                      className="input_box" 
                      type="text" 
                      name="direccionHuesped.pais"
                      value={formData.direccionHuesped.pais}
                      onChange={handleChange} 
                      required 
                    />
                </div>
            </div>
        </div>
        <div className="container">
            <div className="box1">
                <label>Email</label>
                <input 
                  className="input_box" 
                  type="email" 
                  name="email" 
                  value={formData.email}
                  onChange={handleChange}
                />    
            </div>

            <div className="box1">
                <label>Ocupación *</label>
                <input 
                  className="input_box" 
                  type="text" 
                  name="ocupacion" 
                  value={formData.ocupacion}
                  onChange={handleChange}
                required 
                />    
            </div>

            <div className="box1">
                <label>Nacionalidad *</label>
                <input 
                  className="input_box" 
                  type="text" 
                  name="nacionalidad" 
                  value={formData.nacionalidad}
                  onChange={handleChange}
                required 
                />
            </div>         
        </div>    
        
        
        


        <div className="container">
            <div className="box1">
                <button className="button2" type="reset">CANCELAR</button>
            </div>
            <div className="box1">
                <p><small><span style={{color: 'red'}}>*</span> Campos obligatorios</small></p>
            </div>
            <div className="box1">
                <button className="button2" type="submit">SIGUIENTE</button>    
            </div>
        </div> 
      </form>
    </main>
  );
};