export const normalizeTurkishText = (value: string): string => value
  .normalize('NFKC')
  .toLocaleLowerCase('tr-TR')
  .replace(/[^\p{L}\p{N}\s]/gu, ' ')
  .replace(/\s+/g, ' ')
  .trim();

export const questionFingerprintInput = (
  question: string,
  options: string[],
  correctIndex: number,
): string => {
  const normalizedQuestion = normalizeTurkishText(question);
  const normalizedOptions = options.map(normalizeTurkishText);
  return `${normalizedQuestion}|${normalizedOptions.join('|')}|${correctIndex}`;
};
