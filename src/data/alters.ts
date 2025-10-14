export type AlterInfo = {
  id: string;
  name: string;
  backgroundColor: string;
}
export namespace Alters {
  export const KATE: AlterInfo = {id: "kate", name: "Kate", backgroundColor: "#000000"}
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
  export const OTHER: AlterInfo = {id: "other", name: "Other", backgroundColor: "#949494"}

  export const ALL: AlterInfo[] = [
    KATE, RUBY, JADEN, TOPAZ, SAPPHIRE,
    SIXTH, SEVEN, STELLA, CONSTELLATION, IVY, THORNE,
    OTHER,
  ]

  export const HEADSPACE: (AlterInfo | null)[][] = [
      [null, STELLA, SEVEN],
      [KATE, RUBY, null],
      [JADEN, null, SIXTH],
      [TOPAZ, SAPPHIRE, null]
  ]
}
