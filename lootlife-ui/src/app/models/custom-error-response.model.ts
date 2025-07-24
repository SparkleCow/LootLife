export interface CustomHttpError {
  status: number;
  title: string;
  userMessage: string;
  rawMessage?: string;
  validationErrors?: string[];
  details?: Record<string, any>;
}
