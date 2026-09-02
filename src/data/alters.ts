export type AlterInfo = {
  id: string;
  name: string;
  backgroundColor: string;
}

export namespace Alters {
  export const KATE: AlterInfo = {id: "kate", name: "Kate", backgroundColor: "#000000"}
  export const KAYLIN: AlterInfo = {id: "kaylin", name: "Kaylin", backgroundColor: "#3666ff"}
  export const CLOVER: AlterInfo = {id: "clover", name: "Clover", backgroundColor: "#00ab00"}
  export const RUBY: AlterInfo = {id: "ruby", name: "Ruby", backgroundColor: "#BE2627"}
  export const JADEN: AlterInfo = {id: "jaden", name: "Jaden", backgroundColor: "#03750F"}
  export const TOPAZ: AlterInfo = {id: "topaz", name: "Topaz", backgroundColor: "#0A76B4"}
  export const SAPPHIRE: AlterInfo = {id: "sapphire", name: "Sapphire", backgroundColor: "#0F20BB"}
  export const SIXTH: AlterInfo = {id: "sixth", name: "Sixth Part", backgroundColor: "#EB8C1F"}
  export const SEVEN: AlterInfo = {id: "seven", name: "Seven", backgroundColor: "#149a00"}
  export const STELLA: AlterInfo = {id: "stella", name: "Stella", backgroundColor: "#F45DE8"}
  export const CONSTELLATION: AlterInfo = {id: "constellation", name: "Constellation", backgroundColor: "#49009C"}
  export const IVY: AlterInfo = {id: "ivy", name: "Ivy", backgroundColor: "#01A00A"}
  export const THORNE: AlterInfo = {id: "thorne", name: "Thorne", backgroundColor: "#011793"}
  export const SELEE: AlterInfo = {id: "selee", name: "Selee", backgroundColor: "#28089b"}
  export const AMBER: AlterInfo = {id: "amber", name: "Amber", backgroundColor: "#c58900"}
  export const RIPPLE: AlterInfo = {id: "ripple", name: "Ripple", backgroundColor: "#0076fb"}
  export const JUNE: AlterInfo = {id: "june", name: "June", backgroundColor: "#ff4fb0"}
  export const JAMIE: AlterInfo = {id: "jamie", name: "Jamie", backgroundColor: "#00b964"}
  export const PAIGE: AlterInfo = {id: "paige", name: "Paige", backgroundColor: "#009fef"}
  export const ELLIE: AlterInfo = {id: "ellie", name: "Ellie", backgroundColor: "#6f39ff"}
  export const IRIS: AlterInfo = {id: "iris", name: "Iris", backgroundColor: "#0070ff"}
  export const OTHER: AlterInfo = {id: "other", name: "Other", backgroundColor: "#949494"}

  export const ALL: AlterInfo[] = [
    KATE, KAYLIN, CLOVER, RUBY, JADEN, TOPAZ, SAPPHIRE,
    SIXTH, SEVEN, STELLA, CONSTELLATION, IVY, THORNE, 
    SELEE, AMBER, RIPPLE, JUNE, JAMIE, PAIGE, ELLIE, 
    IRIS, OTHER,
  ]
}
