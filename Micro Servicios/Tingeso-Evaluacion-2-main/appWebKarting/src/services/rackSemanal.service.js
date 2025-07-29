import httClient from "../http-common";

const RACK_SEMANAL_API_URL = "/api/rackSemanal"

const horariosDisponiblesSeisMeses = () => {
  return httClient.get(`${RACK_SEMANAL_API_URL}/horariosDisponibles`);
};

const obtenerTodasLasReservasOcupadas = () => {
  return httClient.get(`${RACK_SEMANAL_API_URL}/horariosOcupados`)
};

const obtenerReservaPorFechaYHora = (fechaInicio, horaInicio, horaFin) => {
    return httClient.get(`${RACK_SEMANAL_API_URL}/obtenerReservaPorFechaYHora`, {
      params: {
        fechaInicio,
        horaInicio,
        horaFin,
      }
    });
};

export default {
  horariosDisponiblesSeisMeses,
  obtenerTodasLasReservasOcupadas,
  obtenerReservaPorFechaYHora
};
