import type { Wilayah } from "@/services/wilayahService";

export function normalizeWilayahName(name: string): string {
    return name
        .normalize('NFC')
        .trim()
        .replace(/\s+/g, ' ')
        .toLocaleLowerCase('id-ID');
}

export function findWilayahByName(
    wilayahList: Wilayah[],
    savedName: string | null | undefined
): Wilayah | undefined {
    if (!savedName?.trim()) return undefined;

    const target = normalizeWilayahName(savedName);

    return wilayahList.find(
        wilayah => normalizeWilayahName(wilayah.name) === target
    );
}
