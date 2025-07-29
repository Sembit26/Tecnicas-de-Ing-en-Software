import httClient from "../http-common";

const RESERVA_API_URL = "/api/reportes"

// Obtener ingresos por vueltas
const obtenerIngresosPorVueltas = (fechaInicio, fechaFin) => {
  return httClient.get(`${RESERVA_API_URL}/ingresos-por-vueltas`, {
    params: { fechaInicio, fechaFin }
  });
};

// Obtener ingresos por cantidad de personas
const obtenerIngresosPorPersonas = (fechaInicio, fechaFin) => {
  return httClient.get(`${RESERVA_API_URL}/ingresos-por-grupo`, {
    params: { fechaInicio, fechaFin }
  });
};

export default {
  obtenerIngresosPorVueltas,
  obtenerIngresosPorPersonas,
};
